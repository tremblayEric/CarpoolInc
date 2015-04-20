package com.carpool.ui.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.model.Reservation;
import com.carpool.model.Trajet;
import com.carpool.ui.design.CallbackFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
listes des reservations
 */
public class MesReservationsFragment extends CallbackFragment {
    View rootview;
    private final ArrayList<Reservation> listeReservation = new ArrayList<Reservation>();
    private final HashMap<String, String> listeAdressesDepartOffre = new HashMap<String, String>();
    private final HashMap<String, String> listeAdressesArriveeOffre = new HashMap<String, String>();

    ExpandableListView lv;
    private boolean chargee = false;
    String[] tabDepart;
    String[] tabDestination;

    /**
     * The fragment's current callback object.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }

        @Override
        public int getSelectedFragment() {
            return 0;
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_mes_reservations, container, false);


        return rootview;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = (ExpandableListView) view.findViewById(R.id.expListViewRes);
        Resources res = this.getResources();
        Drawable devider = res.getDrawable(R.drawable.line);
        lv.setGroupIndicator(null);
        lv.setDivider(devider);
        lv.setChildDivider(devider);
        lv.setDividerHeight(1);

        registerForContextMenu(lv);

        final Date dateJour = new Date();
        ParseQuery<Reservation> query = ParseQuery.getQuery("Reservation");
        query.whereEqualTo("userDemandeur", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Reservation>() {
            @Override
            public void done(List<Reservation> reservations, ParseException e) {
                if (e == null) {
                    tabDepart = new String[reservations.size()];
                    tabDestination = new String[reservations.size()];
                    for(Reservation res : reservations){
                        try {
                            res.getOffreSource().fetchIfNeeded();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        if( !res.getOffreSource().getDepart().before(dateJour)){
                            listeReservation.add(res);
                            System.out.println(" isEncourSelected true ----------------- "+res.getObjectId());
                        }
                   }
                    lv.setAdapter(new MyExpandableListAdapter());
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * A Custom adapter to create Parent view (Used grouprow.xml) and Child View((Used childrow.xml).
     */
    private class MyExpandableListAdapter extends BaseExpandableListAdapter
    {

        private LayoutInflater inflater;

        public MyExpandableListAdapter()
        {
            // Create Layout Inflator
            inflater = LayoutInflater.from(getActivity());
        }

        // This Function used to inflate parent rows view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parentView)
        {

            System.out.println("Reservation : "+groupPosition);
            final Reservation reservation = listeReservation.get(groupPosition);
            final  View convertViewLocale = inflater.inflate(R.layout.fragment_mes_reservations, parentView, false);
            Offre offresource =null;
            String  conducteur = "";

            try {
                offresource = reservation.getOffreSource().fetchIfNeeded();
                offresource.getUser().fetchIfNeeded();
                offresource.getTrajet().fetchIfNeeded();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            offresource = reservation.getOffreSource();
            conducteur = offresource.getUser().getUsername();


            Trajet trajetResultat = offresource.getTrajet();
            try {
                trajetResultat.getPositionDepart().fetchIfNeeded();
                trajetResultat.getPositionArrive().fetchIfNeeded();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            double lattDep = trajetResultat.getPositionDepart().getLatitude();
            double longDep = trajetResultat.getPositionDepart().getLongitude();

            double lattArr = trajetResultat.getPositionArrive().getLatitude();
            double longArr = trajetResultat.getPositionArrive().getLongitude();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ((TextView) convertViewLocale.findViewById(R.id.dateOffreSources)).setText(sdf.format(offresource.getDepart()));
            ((TextView) convertViewLocale.findViewById(R.id.conducteurOffreSource)).setText(conducteur);


            class TaskDeparts extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String[] params) {
                    return getRoadName(params);
                }

                protected void onPostExecute(String result) {
                    chargee = true;
                    ((TextView) convertViewLocale.findViewById(R.id.adDepartOffreSource)).setText(result);
                    listeAdressesDepartOffre.put(reservation.getObjectId(),result);

                }

            }

            class TaskDestinations extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String[] params) {
                    return getRoadName(params);
                }

                protected void onPostExecute(String result) {
                    ((TextView) convertViewLocale.findViewById(R.id.adDestinationOffreSource)).setText(result);
                    listeAdressesArriveeOffre.put(reservation.getObjectId(),result);

                }

            }
            if(!chargee){
                new TaskDeparts().execute(String.valueOf(lattDep),String.valueOf(longDep));
                new TaskDestinations().execute(String.valueOf(lattArr),String.valueOf(longArr));
            }
            else{
                ((TextView) convertViewLocale.findViewById(R.id.adDepartOffreSource)).setText(listeAdressesDepartOffre.get(reservation.getObjectId()));
                ((TextView) convertViewLocale.findViewById(R.id.adDestinationOffreSource)).setText(listeAdressesArriveeOffre.get(reservation.getObjectId()));
            }

            return convertViewLocale;
        }

        // This Function used to inflate child rows view
        @Override
        public View getChildView(int groupPosition,  int childPosition, boolean isLastChild,
                                 View convertView,  ViewGroup parentView){
         return null;

        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {

            return null;
        }

        //Call when child row clicked
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
             return 0;
        }


        @Override
        public Object getGroup(int groupPosition)
        {
            Log.i("reservation", groupPosition + "=  getGroup ");
            return listeReservation.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return listeReservation.size();
        }

        //Call when parent row clicked
        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public void notifyDataSetChanged()
        {
            super.notifyDataSetChanged();
        }

        @Override
        public boolean isEmpty()
        {
            return ((listeReservation == null) || listeReservation.isEmpty());
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return false;
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }

    }

    private String getRoadName(String[] params){

        final StringBuilder sb = new StringBuilder();
        final StringBuilder sbRetour = new StringBuilder();

        InputStream inputStream = null;
        String result = null;
        try {
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=__LAT__,__LNG__&sensor=false";

            url = url.replaceAll("__LAT__", params[0]);
            url = url.replaceAll("__LNG__",  params[1]);

            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpGet httpget = new HttpGet(url);

            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception squish) {
            }
        }

        JSONObject jObject = null;
        JSONArray jArray = null;
        try {
            jObject = new JSONObject(result);
            jArray = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jArray != null && jArray.length() > 0) {
            try {
                JSONObject oneObject = jArray.getJSONObject(0);
                // Pulling items from the array
                //sbRetour.append(oneObject.getString("formatted_address"));
                JSONArray address_components  = oneObject.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");

                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);
                    if (Type.equalsIgnoreCase("locality")) {
                        // Address2 = Address2 + long_name + ", ";
                        sbRetour.append(long_name);
                        System.out.println(sbRetour.toString());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sbRetour.toString();

    }
}
