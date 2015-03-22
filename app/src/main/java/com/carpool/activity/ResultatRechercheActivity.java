package com.carpool.activity;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TimePicker;

import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ResultatRechercheActivity extends Fragment {

    View rootview = null;
    final ArrayList<Offre> listOffers = new ArrayList<Offre>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_resultat_recherche,container,false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ExpandableListView listView = (ExpandableListView) rootview.findViewById(R.id.lvResultSearch);


        DatePicker dateDepartSouhaitable = (DatePicker) getActivity().findViewById(R.id.dateRecherche);
        TimePicker tempsDepartSouhaitable = (TimePicker) getActivity().findViewById(R.id.tempsDepartRecherche);



        ParseQuery<Offre> query = ParseQuery.getQuery("Offre");
        //query.whereEqualTo("depart", dateDepartSouhaitable);
        query.findInBackground(
                new FindCallback<Offre>()
                {
                    @Override
                    public void done(List<Offre> offres, ParseException e) {
                        if (e == null) {

                           List<Offre> offresAcceptables= getOffreCorrespondantes(offres);
                           MyResultSearchListAdapter adapter = new MyResultSearchListAdapter(getActivity(), offresAcceptables);
                           listView.setAdapter(adapter);
                        } else {
                            /*exception*/
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Offre> getOffreCorrespondantes(List<Offre> offres){

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
/*
        location2.setLatitude(17.375775);
        location2.setLongitude(78.469218);
        double distance=locationSouhaitable.distanceTo(location2);*/
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
