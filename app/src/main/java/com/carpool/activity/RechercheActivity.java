package com.carpool.activity;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.carpool.utils.GeocodingLocation;
import com.carpool.utils.PlaceAPI;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;


public class RechercheActivity extends Fragment {

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.recherche_layout,container,false);

        AutoCompleteTextView autoCompViewFrom = (AutoCompleteTextView) rootview.findViewById(R.id.etDepart);
        autoCompViewFrom.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));
        AutoCompleteTextView autoCompViewTo = (AutoCompleteTextView) rootview.findViewById(R.id.etSearchDestination);
        autoCompViewTo.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));

        ((TimePicker)rootview.findViewById(R.id.etBetweenStartSearch)).setIs24HourView(true);



        Button submitOffer = (Button)(rootview.findViewById(R.id.btnSubmitSearch));
        submitOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> address = new ArrayList<>();
                address.add(((AutoCompleteTextView)rootview.findViewById(R.id.etDepart)).getText().toString());
                address.add(((AutoCompleteTextView)rootview.findViewById(R.id.etSearchDestination)).getText().toString());

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address,
                        getActivity().getApplicationContext(), new GeocoderHandler());

            }
        });
        return rootview;
    }

    /*
    Tiré de http://javapapers.com/android/android-geocoding-to-get-latitude-longitude-for-an-address/
     */
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            //String locationAddress;
            LinkedHashSet<double[]> locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    //locationAddress = bundle.getString("address");
                    locationAddress = (LinkedHashSet<double[]>)bundle.getSerializable("address");
                    break;
                default:
                    locationAddress = null;
            }
            //saveOffer(locationAddress);
        }
    }

    /*
    Tiré de https://developers.google.com/places/training/autocomplete-android
     */
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;
        PlaceAPI place = new PlaceAPI();

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = place.autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    private void getOffresFromDataBase(){

        final ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.lvResultSearch);

        ParseQuery<Offre> query = ParseQuery.getQuery("Offre");
        query.findInBackground(
                new FindCallback<Offre>() {
                    @Override
                    public void done(List<Offre> offres, ParseException e) {
                        if (e == null) {

                            List<Offre> offresAcceptables = getOffreCorrespondantes(offres);
                            MyResultSearchListAdapter adapter = new MyResultSearchListAdapter(getActivity(), offresAcceptables);
                            listView.setAdapter(adapter);
                        } else {
                            /*exception*/
                        }
                    }
                });
    }

    private List<Offre> getOffreCorrespondantes(List<Offre> offres){
        /*
        Calendar cal = Calendar.getInstance();
        DatePicker dateDepart = (DatePicker)rootview.findViewById(R.id.etDate);
        cal.set(dateDepart.getYear(), dateDepart.getMonth(), dateDepart.getDayOfMonth());

        offre.setDepart(cal.getTime());

        TimePicker timePicker = (TimePicker)rootview.findViewById(R.id.etBetweenStart);
        cal.set(Calendar.HOUR, timePicker.getCurrentHour());
        cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        */

        List<Offre> lesOffres = offres;
        List<Offre> offresAcceptables = new ArrayList<Offre>();

        DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.dateRecherche);
        TimePicker tempsDepart = (TimePicker) getActivity().findViewById(R.id.tempsDepartRecherche);
        List<HashMap<String,String>> list = (List<HashMap<String,String>>)getArguments().get("ListFromMap");

        Location locationSouhaitable = new Location("locationA");

        locationSouhaitable.setLatitude(Double.parseDouble(((HashMap<String, String>)list.get(0)).get("lat")));
        locationSouhaitable.setLongitude(Double.parseDouble(((HashMap<String, String>)list.get(0)).get("lng")));


        for (int i = 0 ; i < lesOffres.size();++i){
            Offre uneOffre = lesOffres.get(i);

            Date dateDuDepart = uneOffre.getDepart();
            long heureDepartAuPlusTot = uneOffre.getHeureDebut().getHours();
            long heureDepartAuPlusTard = uneOffre.getHeureFin().getHours();


            long diffTemps = dateDuDepart.getTime() - getDateFromDatePicker(datePicker).getTime();
            int jourDeDifference = (int) diffTemps / (1000 * 60 * 60 * 24);


            int heureSouhaitable = tempsDepart.getCurrentHour();
            boolean heureSouhaitableConcordeAvecOffre = ((heureDepartAuPlusTot <= heureSouhaitable ) && (heureDepartAuPlusTard >= heureSouhaitable));

            if(jourDeDifference == 0 && heureSouhaitableConcordeAvecOffre){

                try {
                    uneOffre.getTrajet().fetchIfNeeded();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Trajet trajetResultat = uneOffre.getTrajet();
                try {
                    trajetResultat.getPositionDepart().fetchIfNeeded();
                    trajetResultat.getPositionArrive().fetchIfNeeded();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                Location locationOfferte = new Location("locationA");

                locationOfferte.setLatitude(trajetResultat.getPositionDepart().getLatitude());
                locationOfferte.setLongitude(trajetResultat.getPositionDepart().getLongitude());

                double distance = locationSouhaitable.distanceTo(locationOfferte) / 1000;
                if (distance <= 20) {

                    locationSouhaitable.setLatitude(Double.parseDouble(((HashMap<String, String>) list.get(1)).get("lat")));
                    locationSouhaitable.setLongitude(Double.parseDouble(((HashMap<String, String>) list.get(1)).get("lng")));

                    locationOfferte.setLatitude(trajetResultat.getPositionArrive().getLatitude());
                    locationOfferte.setLongitude(trajetResultat.getPositionArrive().getLongitude());
                    distance = locationSouhaitable.distanceTo(locationOfferte) / 1000;
                    if (distance <= 20) {
                        offresAcceptables.add(lesOffres.get(i));
                    }
                }
            }
        }
        return offresAcceptables;
    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}
