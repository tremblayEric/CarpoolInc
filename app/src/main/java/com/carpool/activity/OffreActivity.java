package com.carpool.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import android.util.Log;

import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by Bart on 2015-03-07.
 */
public class OffreActivity extends Fragment {

    View rootview;
    static final int DATE_PICKER_ID = 1111;
    static final int TIME_PICKER_ID_ENTRE = 1112;
    static final int TIME_PICKER_ID_ET = 1113;
    static final int NUM_PICKER_ID = 1114;

    // variables pour le datePicker
    private int year;
    private int month;
    private int day;


    private int year_depart;
    private int month_depart;
    private int day_depart;

    //variable pour les TimePicker
    private int hour_entre;
    private int minute_entre;

    private int hour_et;
    private int minute_et;

    //variables pour le numeric
    private int number;


    private TimePickerDialog timePickeraDialog_entre;
    private TimePickerDialog timePickerDialog_et;

    TimePickerDialog.OnTimeSetListener timePickerListenerEntre;
    TimePickerDialog.OnTimeSetListener timePickerListenerEt;

    //variables
    AutoCompleteTextView ac_etBetweenEnd;
    AutoCompleteTextView ac_etBetweenStart;
    AutoCompleteTextView etNbreProp;
    AutoCompleteTextView etDate_;

    DatePickerDialog mDatePicker;
    Calendar c_date;
    Calendar c_time_entre;
    Calendar c_time_et;

    AutoCompleteTextView autoCompViewFrom;
    AutoCompleteTextView autoCompViewTo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.offre_layout, container, false);

         autoCompViewFrom = (AutoCompleteTextView) rootview.findViewById(R.id.etStarting);
        autoCompViewFrom.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));
         autoCompViewTo = (AutoCompleteTextView) rootview.findViewById(R.id.etDestination);
        autoCompViewTo.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_autocomplete));

        // NumericPicker nbre de places
        etNbreProp = (AutoCompleteTextView) rootview.findViewById(R.id.etNbreProposition);


        /*
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                calendar.set(selectedyear, selectedmonth, selectedday);
                dateDepart.setText(new StringBuilder().append(selectedmonth + 1)
                        .append("-").append(selectedday).append("-").append(selectedyear)
                        .append(" "));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Date de départ");
        mDatePicker.show();*/

        // DatePicker date de depart
        /****
         *  Gestion de la date de dpart
         */
        etDate_ = (AutoCompleteTextView)rootview.findViewById(R.id.etDate);

        // Get current date by calender
        c_date = Calendar.getInstance();
        year_depart  = c_date.get(Calendar.YEAR);
        month_depart = c_date.get(Calendar.MONTH);
        day_depart   = c_date.get(Calendar.DAY_OF_MONTH);


        etDate_.setText(new StringBuilder().append(month_depart + 1)
                .append("-").append(day_depart).append("-").append(year_depart)
                .append(" "));
        mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                c_date.set(selectedyear, selectedmonth, selectedday);

                // si ca se trouve ca sert a rien mais je le fais qd meme
                year_depart= selectedyear;
                month_depart = selectedmonth;
                day_depart = selectedday;

                etDate_.setText(new StringBuilder().append(selectedmonth + 1)
                        .append("-").append(selectedday).append("-").append(selectedyear)
                        .append(" "));
            }
        }, year_depart, month_depart, day_depart);

        etDate_.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                mDatePicker.setTitle("Date de départ");
                mDatePicker.show();

                Log.d("log","dans le logId");
                Log.d("anee", "anne "+ year_depart);
                Log.d("mois", "anne "+ month_depart);
                Log.d("jour", "anne "+ day_depart);

                etDate_.requestFocus();

                return false;
            }
        });


        /**
         * Gestion du temps de depart
         */

        // Get current date by calender
        c_time_entre = Calendar.getInstance();
        hour_entre  = c_time_entre.get(Calendar.HOUR_OF_DAY);
        minute_entre = c_time_entre.get(Calendar.MINUTE);
        ac_etBetweenStart = (AutoCompleteTextView)rootview.findViewById(R.id.etBetweenStart);
        //ac_etBetweenStart.setText(hour_entre+":"+minute_entre);
        ac_etBetweenStart.setText(new StringBuilder().append(padding_str(hour_entre))
                .append(":").append(padding_str(minute_entre)));

       timePickerListenerEntre =  new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour_entre = selectedHour;
                minute_entre = selectedMinute;

                // set current time into textview
                ac_etBetweenStart.setText(new StringBuilder().append(padding_str(hour_entre))
                        .append(":").append(padding_str(minute_entre)));

            }
        };

        timePickeraDialog_entre = new TimePickerDialog(getActivity(), timePickerListenerEntre, hour_entre, minute_entre, false);

        ac_etBetweenStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               timePickeraDialog_entre.show();

                Log.d("log","dans le logId");
                Log.d("heure depart", "heure "+ hour_entre);
                Log.d("minute depart", "minute "+ minute_entre);

                ac_etBetweenStart.requestFocus();
                return false;
            }
        });


        /***
         * Gestion du temps d'arrivee
         */
        //TimePicker et

        ac_etBetweenEnd = (AutoCompleteTextView)rootview.findViewById(R.id.etBetweenEnd);
        c_time_et =  Calendar.getInstance();

        hour_et = c_time_et.get(Calendar.HOUR_OF_DAY);
        minute_et = c_time_et.get(Calendar.MINUTE);

        ac_etBetweenEnd.setText(new StringBuilder().append(padding_str(hour_et))
                .append(":").append(padding_str(minute_et)));


        timePickerListenerEt =  new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour_et = selectedHour;
                minute_et = selectedMinute;

                // set current time into textview
                ac_etBetweenEnd.setText(new StringBuilder().append(padding_str(hour_et))
                        .append(":").append(padding_str(minute_et)));

            }
        };

        timePickerDialog_et = new TimePickerDialog(getActivity(), timePickerListenerEt, hour_et, minute_et, false);

        ac_etBetweenEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                timePickerDialog_et.show();

                Log.d("log","dans le logId");
                Log.d("heure arrivee", "heure "+ hour_et);
                Log.d("minute arrivee", "minute "+ minute_et);

                ac_etBetweenEnd.requestFocus();

                return true;
            }
        });


       // ((NumberPicker)rootview.findViewById(R.id.etNbreProposition)).setMinValue(1);
       // ((NumberPicker)rootview.findViewById(R.id.etNbreProposition)).setMaxValue(10);


        Button submitOffer = (Button)(rootview.findViewById(R.id.btnSubmitOffer));

        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
                "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );

        submitOffer.append("     Ajouter Offre");

        submitOffer.setTypeface(font);

        submitOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> address = new ArrayList<>();

                String eStarting = autoCompViewFrom.getText().toString();
                String eDestination = autoCompViewTo.getText().toString();
                String nbProp = etNbreProp.getText().toString();

                if (eStarting.length() > 0 && eDestination.length() >0 && nbProp.length()>0) {
                    address.add(eStarting);
                    address.add(eDestination);


                    GeocodingLocation locationAddress = new GeocodingLocation();
                    locationAddress.getAddressFromLocation(address,
                            getActivity().getApplicationContext(), new GeocoderHandler());
                }
                else {
                    if (eStarting.length() == 0) {
                        autoCompViewTo.setError("champ obligatoire");
                        autoCompViewTo.requestFocus();
                    } else {
                        autoCompViewTo.setError(null);
                    }

                    if (eDestination.length() == 0) {
                        autoCompViewFrom.setError("Champ obligatoire");
                        autoCompViewFrom.requestFocus();
                    }else
                    {
                        autoCompViewFrom.setError(null);
                    }
                    if(nbProp.length() == 0)
                    {
                        etNbreProp.setError("Champ obligatoire");
                        etNbreProp.requestFocus();
                    }
                    else
                    {
                        etNbreProp.setError(null);
                    }
                }


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

        /*
        Calendar cal = Calendar.getInstance();
        DatePicker dateDepart = (DatePicker)rootview.findViewById(R.id.etDate);
        cal.set(dateDepart.getYear(), dateDepart.getMonth(), dateDepart.getDayOfMonth());*/

        offre.setDepart(c_date.getTime());

        //TimePicker timePicker = (TimePicker)rootview.findViewById(R.id.etBetweenStart);
        //cal.set(Calendar.HOUR, timePicker.getCurrentHour());
        //cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        offre.setHeureDebut(c_time_entre.getTime());

       // timePicker = (TimePicker)rootview.findViewById(R.id.etBetweenEnd);
        //cal.set(Calendar.HOUR, timePicker.getCurrentHour());
        //cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        offre.setHeureFin(c_time_et.getTime());

        //NumberPicker numberPicker = (NumberPicker)rootview.findViewById(R.id.etNbreProposition);
        offre.setNbreProposition(Integer.valueOf(etNbreProp.getText().toString()));//numberPicker.getValue());
        offre.setReservationCount(Integer.valueOf(etNbreProp.getText().toString()));//numberPicker.getValue());

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

/*
    //@Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(getActivity(), pickerListener, year_depart, month_depart,day_depart);

            case TIME_PICKER_ID_ENTRE:
                return new TimePickerDialog(getActivity(), timePickerListenerEntre, hour_entre, minute_entre,false);

            case TIME_PICKER_ID_ET:
                return new TimePickerDialog(getActivity(), timePickerListenerEt, hour_et, minute_et, false);

            case NUM_PICKER_ID:
                //return new NumberPicker();
                Log.d("number_picker", "dans le number picker");
                break;
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener timePickerListenerEntre =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
        hour_entre = selectedHour;
        minute_entre = selectedMinute;

        // set current time into textview
        ac_etBetweenStart.setText(new StringBuilder().append(padding_str(hour_entre))
                .append(":").append(padding_str(minute_entre)));

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListenerEt =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour_et = selectedHour;
            minute_et = selectedMinute;

            // set current time into textview
            ac_etBetweenEnd.setText(new StringBuilder().append(padding_str(hour_et))
                    .append(":").append(padding_str(minute_et)));

        }
    };*/


    private static String padding_str(int c)
    {
        if (c >= 10)
          return String.valueOf(c);
        else
        return "0" + String.valueOf(c);
    }

   /* private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener()
    {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year_depart  = selectedYear;
            month_depart = selectedMonth;
            day_depart   = selectedDay;

            // SOutputhow selected date
            etDate_.setText(new StringBuilder().append(month_depart + 1)
                    .append("-").append(day_depart).append("-").append(year_depart)
                    .append(" "));

        }
    }; */

}
