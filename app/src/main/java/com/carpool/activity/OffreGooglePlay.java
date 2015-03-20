package com.carpool.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.carpool.GeocodeJSONParser;
import com.carpool.model.Offre;
import com.carpool.model.Trajet;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * cette classe reprend le fonctionnement de la classe adresse, mais dans le contexte de l'offre au
 * lieu de la recherche
 * les deux devrait être factorisées et fusionnées en une seule classe, mais je n'ai pas le temps
 * lors de la phase 1. Navré pour le 'big pile of mud'
 *
 */
public class OffreGooglePlay extends FragmentActivity {

    Button mBtnFind;
    GoogleMap mMap;
    EditText depart;
    EditText arrivee;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre_google_play);

        // Getting reference to the find button
        mBtnFind = (Button) findViewById(R.id.btn_offrir);

        // Getting reference to the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment)(getSupportFragmentManager().findFragmentById(R.id.map_offre));

        // Getting reference to the Google Map
        mMap = mapFragment.getMap();
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        // Getting reference to EditText
        depart = (EditText) findViewById(R.id.depart_offert);
        arrivee = (EditText) findViewById(R.id.arrivee_offert);

        // Setting click event listener for the find button
        mBtnFind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Getting the place entered
                String locationDepart = depart.getText().toString();
                String locationArrivee = arrivee.getText().toString();

                if(locationArrivee==null || locationArrivee.equals("")){
                    Toast.makeText(getBaseContext(), "Entrez une destination d'arrivée", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(locationDepart==null || locationDepart.equals("")){
                    Toast.makeText(getBaseContext(), "Entrez une destination de départ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "https://maps.googleapis.com/maps/api/geocode/json?";

                try {
                    // encoding special characters like space in the user input place
                    locationDepart = URLEncoder.encode(locationDepart, "utf-8");
                    locationArrivee = URLEncoder.encode(locationArrivee, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String addressDepart = "address=" + locationDepart;
                String addressArrivee = "address=" + locationArrivee;

                String sensor = "sensor=false";

                // url , from where the geocoding data is fetched
                String urlDepart = url + addressDepart + "&" + sensor;
                String urlArrivee = url + addressArrivee + "&" + sensor;

                // Instantiating DownloadTask to get places from Google Geocoding service
                // in a non-ui thread
                DownloadTask downloadDepart = new DownloadTask();
                DownloadTask downloadArrivee = new DownloadTask();

                // Start downloading the geocoding places
                downloadDepart.execute(urlDepart,urlArrivee);
            }
        });
    }

    private  String[] downloadUrl(String strUrlDepart, String strUrlArrivee) throws IOException{
        String[] data = new String[2];
        InputStream iStreamDepart = null;
        InputStream iStreamArrive = null;
        HttpURLConnection urlConnectionDepart = null;
        HttpURLConnection urlConnectionArrive = null;
        try{
            URL urlDepart = new URL(strUrlDepart);
            URL urlArrive = new URL(strUrlArrivee);

            // Creating an http connection to communicate with url
            urlConnectionDepart = (HttpURLConnection) urlDepart.openConnection();
            urlConnectionArrive = (HttpURLConnection) urlArrive.openConnection();

            // Connecting to url
            urlConnectionDepart.connect();
            urlConnectionArrive.connect();

            // Reading data from url
            iStreamDepart = urlConnectionDepart.getInputStream();
            iStreamArrive = urlConnectionArrive.getInputStream();

            BufferedReader brDepart = new BufferedReader(new InputStreamReader(iStreamDepart));
            BufferedReader brArrive = new BufferedReader(new InputStreamReader(iStreamArrive));

            StringBuffer sbDepart = new StringBuffer();
            StringBuffer sbArrive = new StringBuffer();

            String lineDepart = "";
            String lineArrive = "";
            while( ( lineDepart = brDepart.readLine()) != null){
                sbDepart.append(lineDepart);
            }

            while( ( lineArrive = brArrive.readLine()) != null){
                sbArrive.append(lineArrive);
            }

            data[0] = sbDepart.toString();
            data[1] = sbArrive.toString();

            brDepart.close();
            brArrive.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStreamDepart.close();
            iStreamArrive.close();
            urlConnectionDepart.disconnect();
            urlConnectionArrive.disconnect();
        }

        return data;
    }

    /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String[]> {

        String[] urls = null;

        // Invoked by execute() method of this object
        @Override
        protected String[] doInBackground(String... url) {
            try{
                urls = downloadUrl(url[0],url[1]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return urls;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String[] result){

            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /** A class to parse the Geocoding Places in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> result = null;
            List<HashMap<String, String>> depart = null;
            List<HashMap<String, String>> arrive = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);
                depart = parser.parse(jObject);
                jObject = new JSONObject(jsonData[1]);
                arrive = parser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }

            result = depart;

            int count = arrive.size();
            for (int i = 0 ; i < count; ++i){
                result.add(arrive.get(i));
            }


            return result;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            /*
             * les position retournees par googl sont dans la liste reçue en parametre
             *
             */
            DatePicker datePicker = (DatePicker) findViewById(R.id.date_offre);
            TimePicker tempsDepart = (TimePicker) findViewById(R.id.temps_Depart_offert);
            TimePicker tempsArrivee = (TimePicker) findViewById(R.id.temps_Arrivee_offert);

            //utiliser les donne plus heut pour enregistrer la nouvelle position



            /*
            Fragment resultSearchFragment = new ResultatRechercheActivity();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ListFromMap", (Serializable)list);
            resultSearchFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_activity_adresse, resultSearchFragment)
                    .commit();*/


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}




