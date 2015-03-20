package com.carpool.activity;

import android.content.Context;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.carpool.utils.GeocodingLocation;
import com.carpool.utils.PlaceAPI;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Bart on 2015-03-07.
 */
public class OffreActivity extends Fragment{

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.offre_layout,container,false);

        AutoCompleteTextView autoCompViewFrom = (AutoCompleteTextView) rootview.findViewById(R.id.etStarting);
        autoCompViewFrom.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));
        AutoCompleteTextView autoCompViewTo = (AutoCompleteTextView) rootview.findViewById(R.id.etDestination);
        autoCompViewTo.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));

        ((TimePicker)rootview.findViewById(R.id.etBetweenStart)).setIs24HourView(true);
        ((TimePicker)rootview.findViewById(R.id.etBetweenEnd)).setIs24HourView(true);

        ((NumberPicker)rootview.findViewById(R.id.etNbreProposition)).setMinValue(1);
        ((NumberPicker)rootview.findViewById(R.id.etNbreProposition)).setMaxValue(10);

        Button submitOffer = (Button)(rootview.findViewById(R.id.btnSubmitOffer));
        submitOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> address = new ArrayList<>();
                address.add(((AutoCompleteTextView)rootview.findViewById(R.id.etStarting)).getText().toString());
                address.add(((AutoCompleteTextView)rootview.findViewById(R.id.etDestination)).getText().toString());

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address,
                        getActivity().getApplicationContext(), new GeocoderHandler());


            }
        });
        return rootview;
    }



    private void saveOffer(LinkedHashSet<double[]> locationAddress){
        Position depart = new Position();
        depart.setLatitude(((double[])locationAddress.toArray()[0])[0]);
        depart.setLongitude(((double[])locationAddress.toArray()[0])[1]);

        Position arrivee = new Position();
        arrivee.setLatitude(((double[])locationAddress.toArray()[1])[0]);
        arrivee.setLongitude(((double[])locationAddress.toArray()[1])[1]);

        Trajet trajet = new Trajet();
        trajet.setPositionDepart(depart);
        trajet.setPositionArrive(arrivee);

        Offre offre = new Offre() ;

        Calendar cal = Calendar.getInstance();
        DatePicker dateDepart = (DatePicker)rootview.findViewById(R.id.etDate);
        cal.set(dateDepart.getYear(), dateDepart.getMonth(), dateDepart.getDayOfMonth());

        offre.setDepart(cal.getTime());

        TimePicker timePicker = (TimePicker)rootview.findViewById(R.id.etBetweenStart);
        cal.set(Calendar.HOUR, timePicker.getCurrentHour());
        cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        offre.setHeureDebut(cal.getTime());

        timePicker = (TimePicker)rootview.findViewById(R.id.etBetweenEnd);
        cal.set(Calendar.HOUR, timePicker.getCurrentHour());
        cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        offre.setHeureFin(cal.getTime());

        NumberPicker numberPicker = (NumberPicker)rootview.findViewById(R.id.etNbreProposition);
        offre.setNbreProposition(numberPicker.getValue());
        offre.setReservationCount(numberPicker.getValue());

        offre.setTrajet(trajet);
        offre.setUser(ParseUser.getCurrentUser());

        offre.saveInBackground();

        Fragment objFragment = new ConsultationOffreActivity();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();
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
            saveOffer(locationAddress);
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
}
