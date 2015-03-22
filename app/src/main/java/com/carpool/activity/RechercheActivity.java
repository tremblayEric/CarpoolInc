package com.carpool.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.NumberPicker;
import android.widget.TextView;
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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;


public class RechercheActivity extends Fragment {

    View rootview;
    ExpandableListView listView;
    final Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.recherche_layout,container,false);
        listView = (ExpandableListView) rootview.findViewById(R.id.lvResultSearch);

        AutoCompleteTextView autoCompViewFrom = (AutoCompleteTextView) rootview.findViewById(R.id.etDepart);
        autoCompViewFrom.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));
        AutoCompleteTextView autoCompViewTo = (AutoCompleteTextView) rootview.findViewById(R.id.etSearchDestination);
        autoCompViewTo.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));

        ((TimePicker)rootview.findViewById(R.id.etBetweenStartSearch)).setIs24HourView(true);

        final EditText dateDepart = (EditText)rootview.findViewById(R.id.etDateSearch);

        dateDepart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        calendar.set(selectedyear, selectedmonth, selectedday);
                        dateDepart.setText(new StringBuilder().append(selectedmonth+1)
                                .append("-").append(selectedday).append("-").append(selectedyear)
                                .append(" "));
                    }
                }, mYear, mMonth, mDay);

                mDatePicker.setTitle("Date de départ");
                mDatePicker.show();
                return false;
            }

        });

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
            LinkedHashSet<double[]> locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = (LinkedHashSet<double[]>)bundle.getSerializable("address");
                    getOffresFromDataBase(locationAddress);
                    break;
                default:
                    locationAddress = null;
            }
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

    private void getOffresFromDataBase(final LinkedHashSet<double[]> locationAddress){

        ParseQuery<Offre> query = ParseQuery.getQuery("Offre");
        query.findInBackground(
                new FindCallback<Offre>() {
                    @Override
                    public void done(List<Offre> offres, ParseException e) {
                        if (e == null) {

                            List<Offre> offresAcceptables = getOffreCorrespondantes(offres,locationAddress);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("offres", (Serializable)offres);
                            Fragment objFragment = new ResultatRechercheActivity();
                            objFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, objFragment)
                                    .commit();
                        } else {
                            //*exception*//*
                        }
                    }
                });
    }

    private List<Offre> getOffreCorrespondantes(List<Offre> offres,LinkedHashSet<double[]> locationAddress){

        List<Offre> lesOffres = offres;
        List<Offre> offresAcceptables = new ArrayList<Offre>();

        EditText textDate = (EditText) getActivity().findViewById(R.id.etDateSearch);
        /*DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date dateDepart = null;
        try {
            dateDepart = format.parse(textDate.toString());
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }*/
        TimePicker tempsDepart = (TimePicker) getActivity().findViewById(R.id.etBetweenStartSearch);

        Location locationSouhaitable = new Location("locationA");

        Iterator<double[]> iter = locationAddress.iterator();
        double[] depart = iter.next();
        double[] arrivee = iter.next();

        locationSouhaitable.setLatitude(depart[0]);
        locationSouhaitable.setLongitude(depart[1]);

        for (int i = 0 ; i < lesOffres.size();++i){
            Offre uneOffre = lesOffres.get(i);

            Date dateDuDepart = uneOffre.getDepart();
            long heureDepartAuPlusTot = uneOffre.getHeureDebut().getHours();
            long heureDepartAuPlusTard = uneOffre.getHeureFin().getHours();


            //long diffTemps = dateDuDepart.getTime() - getDateFromDatePicker(datePicker).getTime();
            long diffTemps = dateDuDepart.getTime() - calendar.getTime().getTime() ;
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

                    locationSouhaitable.setLatitude(arrivee[0]);
                    locationSouhaitable.setLongitude(arrivee[1]);

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
