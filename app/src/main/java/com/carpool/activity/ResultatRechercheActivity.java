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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
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
        ParseQuery<Offre> query = ParseQuery.getQuery(Offre.class);
        //query.whereGreaterThanOrEqualTo("dateDepart", datePicker) ;
        query.include("trajet");
        query.findInBackground(
                new FindCallback<Offre>()
                {
                    @Override
                    public void done(List<Offre> offres, ParseException e) {
                        if (e == null) {
                            List<Offre> cinqOffresMax = getCinqOffreCorrespondantes(offres);
                            MyResultSearchListAdapter adapter = new MyResultSearchListAdapter(getActivity(), offres);
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

    private List<Offre> getCinqOffreCorrespondantes(List<Offre> offres){

        List<Offre> lesOffres = offres;

        List<Offre> cinqPlusPetitesDistances = new ArrayList<Offre>();

        DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.dateRecherche);
        TimePicker tempsDepart = (TimePicker) getActivity().findViewById(R.id.tempsDepartRecherche);
        List<HashMap<String,String>> list = (List<HashMap<String,String>>)getArguments().get("ListFromMap");

        Location locationSouhaitable = new Location("locationA");

        locationSouhaitable.setLatitude(Double.parseDouble(((HashMap<String, String>)list.get(0)).get("lat")));
        locationSouhaitable.setLongitude(Double.parseDouble(((HashMap<String, String>)list.get(0)).get("lng")));

        double distanceMAX = getDistanceMAX(locationSouhaitable,cinqPlusPetitesDistances);

        for (int i = 0 ; i < lesOffres.size();++i){
            Location locationOfferte = new Location("locationA");
            locationOfferte.setLatitude(lesOffres.get(i).getTrajet().getPositionArrive().getLatitude());
            locationOfferte.setLongitude(lesOffres.get(i).getTrajet().getPositionArrive().getLongitude());
            double distance = locationSouhaitable.distanceTo(locationOfferte);
            if(distance <= 5 && lesOffres.size() < 5){
                cinqPlusPetitesDistances.add(lesOffres.get(i));
            }else if(distance <= 5 && distance < distanceMAX){
                distanceMAX = distance;
                cinqPlusPetitesDistances = replaceMAX(locationSouhaitable,lesOffres.get(i),cinqPlusPetitesDistances);
            }
        }
/*
        location2.setLatitude(17.375775);
        location2.setLongitude(78.469218);
        double distance=locationSouhaitable.distanceTo(location2);*/
return null;
    }

    private double getDistanceMAX(Location locationSouhaitable,List<Offre> offres){
double max = 0;

for (int i = 0; i < offres.size(); ++i){
    Location locationOfferte = new Location("locationA");
    locationOfferte.setLatitude(offres.get(i).getTrajet().getPositionArrive().getLatitude());
    locationOfferte.setLongitude(offres.get(i).getTrajet().getPositionArrive().getLongitude());
    double distance = locationSouhaitable.distanceTo(locationOfferte);
    if(distance > max){
        max = distance;
    }
}
        return max;
    }

   private  List<Offre>  replaceMAX(Location locationSouhaitable,Offre offre, List<Offre> offres){

       int indexMax =  findIndexMax(locationSouhaitable,offres);
       offres.remove(indexMax);
       offres.add(offre);

        return offres;
    }

    private int findIndexMax(Location locationSouhaitable,List<Offre> offres){

        int index = 0;
        double max = 0;

        for (int i = 0; i < offres.size(); ++i){
            Location locationOfferte = new Location("locationA");
            locationOfferte.setLatitude(offres.get(i).getTrajet().getPositionArrive().getLatitude());
            locationOfferte.setLongitude(offres.get(i).getTrajet().getPositionArrive().getLongitude());
            double distance = locationSouhaitable.distanceTo(locationOfferte);
            if(distance > max){
                max = distance;
                index = i;
            }
        }
        return index;
    }

}
