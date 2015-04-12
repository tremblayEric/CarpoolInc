package com.carpool.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.ui.adapters.SampleAdapter;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.design.SlidingTabLayout;
import com.carpool.ui.fragments.RechercheResultatFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.List;

public class RechercheResultatActivity extends AccueilActivity implements
        CallbackFragment.Callbacks  {


    public static ArrayList<String> listOffreId;
    public static Offre offreSelectionne;
    private static  LatLng DEPART = new LatLng(40.722543,-73.998585);
    private static  LatLng ARRIVEE = new LatLng(40.7064, -74.0094);
    GoogleMap MAP;

    @Override
    public void onItemSelected(long id) {
    }

    @Override
    public int getSelectedFragment() {
        return AccueilActivity.NAVDRAWER_ITEM_PROFIL_ACTIVITY1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(buildAdapter());
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);//res.getColor(Color.WHITE));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
                int p = position;
            }

            @Override
            public void onPageSelected(int position){
                int i = 0;
            }

            @Override
            public void onPageScrollStateChanged( int state){
                //statique ... cause prob. pour mettre la carte a jour ...
                //RechercheResultatFragment.
                //ViewPager mViewPager = (ViewPager) findViewById(R.id.map);
                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                MAP = mapFragment.getMap();
                //RechercheResultatFragment.rafraichirCarte(map);
                getTrajetAAfficher();
                MarkerOptions options = new MarkerOptions();
                options.position(DEPART);
                options.position(ARRIVEE);
                MAP.addMarker(options);
                String url = getMapsApiDirectionsUrl();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);

                MAP.moveCamera(CameraUpdateFactory.newLatLngZoom(DEPART,
                        13));
                addMarkers(MAP);
                mapFragment.onResume();
               // Fragment fff = ((FragmentPagerAdapter)mViewPager.getAdapter()).getItem(0);
                //fff.onResume();
            }
            private void addMarkers(GoogleMap map) {
                if (map != null) {
                    map.addMarker(new MarkerOptions().position(DEPART)
                            .title("Second Point"));
                    map.addMarker(new MarkerOptions().position(ARRIVEE)
                            .title("Third Point"));
                }
            }

            class ReadTask extends AsyncTask<String, Void, String> {
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

            class HttpConnection {
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

            class ParserTask extends
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
                        polyLineOptions.width(2);
                        polyLineOptions.color(Color.BLUE);
                    }
                    MAP.addPolyline(polyLineOptions);
                }
            }

            class PathJSONParser {

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
        });

        Intent myIntent = getIntent();
        Bundle bundleOffre = myIntent.getExtras();
        listOffreId = bundleOffre.getStringArrayList("offres");
    }

    public static String getMapsApiDirectionsUrl() {

        //remplacer les param en dure par ceux de la liste de l'autre onglet
        String waypoints = "origin=" + DEPART.latitude + "," + DEPART.longitude
                + "&destination=" + ARRIVEE.latitude + "," + ARRIVEE.longitude + "&waypoints=optimize:true|"
                + DEPART.latitude + "," + DEPART.longitude
                + "|" + "|" + ARRIVEE.latitude + ","
                + ARRIVEE.longitude;

        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private static void getTrajetAAfficher(){

        if(RechercheResultatActivity.offreSelectionne != null){
            Offre o =  RechercheResultatActivity.offreSelectionne;
            Position positionDepart = o.getTrajet().getPositionDepart();
            Position positionArrivee = o.getTrajet().getPositionArrive();

            DEPART = new LatLng(positionDepart.getLatitude(),
                    positionDepart.getLongitude());
            ARRIVEE = new LatLng(positionArrivee.getLatitude(), positionArrivee.getLongitude());
        }
    }

    private PagerAdapter buildAdapter() {
        return(new SampleAdapter(this, getSupportFragmentManager()));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
    }


    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return super.canSwipeRefreshChildScrollUp();
    }


    @Override protected int getLayoutResource() {
        return R.layout.tabs_layout;
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return AccueilActivity.NAVDRAWER_ITEM_RECHERCHE;
    }
}
