package com.carpool.ui.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.widget.CompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.carpool.ui.activities.RechercheResultatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.carpool.utils.*;

import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RechercheResultatFragment extends Fragment {
    private SupportMapFragment fragment;
    private GoogleMap map;

    private static final String KEY_POSITION = "position";
    View result;
    int position_;


    public static RechercheResultatFragment newInstance(int position) {
        RechercheResultatFragment frag = new RechercheResultatFragment();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }

    public static String getTitle(int position) {

        switch (position)
        {
            case 0: return String.format("VUE LISTE");
            case 1: return String.format("VUE CARTE");
        }
        // gerer la position
        return ""; //(String.format("position at %d", position + 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        int position = getArguments().getInt(KEY_POSITION, -1);
        position_ = position;

        CompatTextView textView;
        switch (position)
        {
            case 0:
                result = inflater.inflate(R.layout.activity_resultat_recherche, null);
                return (result);
            case 1:
                result = inflater.inflate(R.layout.google_card_fragment, null);
                return (result);
        }
        return null;
    }

    public static String getMapsApiDirectionsUrl(LatLng depart, LatLng arrivee) {
        //remplacer les param en dure par ceux de la liste de l'autre onglet
        String waypoints = "origin=" + depart.latitude + "," + depart.longitude
                + "&destination=" + arrivee.latitude + "," + arrivee.longitude + "&waypoints=optimize:true|"
                + depart.latitude + "," + depart.longitude
                + "|" + arrivee.latitude + ","
                + arrivee.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private void addMarkers(Offre offre,LatLng depart, LatLng arrivee) {
        if (map != null) {
            try {
                map.addMarker(new MarkerOptions().position(depart)
                        .title("Offert par : " + offre.getUser().fetchIfNeeded().getUsername() + "<" + offre.getUser().fetchIfNeeded().getEmail() + ">").snippet(
                                "Départ entre :" + offre.getHeureDebut() + "\n" +
                                        "et : " + offre.getHeureFin()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                map.addMarker(new MarkerOptions().position(arrivee)
                        .title("Arrivée de l'offre de : " + offre.getUser().fetchIfNeeded().getUsername()).snippet(
                                "<" + offre.getUser().fetchIfNeeded().getEmail() + ">"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                polyLineOptions.addAll(points);
                polyLineOptions.width(5);
                polyLineOptions.color(randomColor());
            }
            map.addPolyline(polyLineOptions);
        }
    }

    public int randomColor()
    {
        Random random=new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return  color ;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (position_)
        {
            case 0:     // vue liste

                        final ExpandableListView listView = (ExpandableListView) result.findViewById(R.id.lvResultSearch);
                        Bundle bundle = getArguments();


                        ParseQuery.getQuery(Offre.class)
                                .fromLocalDatastore()
                                .whereContainedIn("objectId", RechercheResultatActivity.listOffreId)
                                .findInBackground(new FindCallback<Offre>() {
                                    @Override
                                    public void done(List<Offre> offres, ParseException e) {
                                        if (offres != null) {
                                            Log.d("Activity1Fragment", String.valueOf(offres.size()));
                                        }else{
                                            Log.d("Activity1Fragment", "liste vide");
                                        }

                                        MyResultSearchListAdapter adapter = new MyResultSearchListAdapter(getActivity(), offres);
                                        listView.setAdapter(adapter);
                                        listView.setBackgroundColor(Color.WHITE);
                                    }
                                });
                break;

            case 1: // vue carte

                break;
        }

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (position_)
        {
            case 1:
                FragmentManager fm = getChildFragmentManager();
                fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
                if (fragment == null) {
                    fragment = SupportMapFragment.newInstance();
                    fm.beginTransaction().replace(R.id.map, fragment).commit();
                }
                break;
        }
    }

    public void tracerTrajets(){

        ParseQuery.getQuery(Offre.class)
                .fromLocalDatastore()
                .whereContainedIn("objectId", RechercheResultatActivity.listOffreId)
                .findInBackground(new FindCallback<Offre>() {
                    @Override
                    public void done(List<Offre> offres, ParseException e) {
                        if (offres != null) {
                                map = fragment.getMap();
                                if(offres != null) {

                                    Iterator<Offre> iterator = offres.iterator();
                                    boolean flag = false;

                                    while (iterator.hasNext()) {
                                        Offre uneOffre = iterator.next();

                                        LatLng depart = new LatLng(uneOffre.getTrajet().getPositionDepart().getLatitude(),
                                                uneOffre.getTrajet().getPositionDepart().getLongitude());
                                        LatLng arrivee = new LatLng(uneOffre.getTrajet().getPositionArrive().getLatitude(),
                                                uneOffre.getTrajet().getPositionArrive().getLongitude());

                                        MarkerOptions options = new MarkerOptions();
                                        options.position(depart);
                                        options.position(arrivee);
                                        map.addMarker(options);
                                        String url = getMapsApiDirectionsUrl(depart, arrivee);
                                        ReadTask downloadTask = new ReadTask();
                                        downloadTask.execute(url);
                                        if (!flag) {
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(depart, 13));
                                        }
                                        addMarkers(uneOffre,depart, arrivee);
                                    }
                                }

                            Log.d("Activity1Fragment", String.valueOf(offres.size()));
                        }else{
                            Log.d("Activity1Fragment", "liste vide");
                        }
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        switch(position_) {
            case 1:
                if (fragment != null && map == null) {
                    map = fragment.getMap();
                    tracerTrajets();
                }
                break;

        }
    }

    public class HttpConnection {
        public String readUrl(String mapsApiDirectionsUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mapsApiDirectionsUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            } catch (Exception e) {
                Log.d("Exception while reading url", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

    }

    public class PathJSONParser {

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {
                jRoutes = jObject.getJSONArray("routes");
                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                    .get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
            return routes;
        }

        /**
         * Method Courtesy :
         * jeffreysambells.com/2010/05/27
         * /decoding-polylines-from-google-maps-direction-api-with-java
         * */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }
}