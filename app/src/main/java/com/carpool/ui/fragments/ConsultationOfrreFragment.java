package com.carpool.ui.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.carpool.model.User;
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
 * classe qui permet de recuperer les informations de l'utilisateur qui se connecte dans la DB et
 * qui les affiche sur le profil de l'utilisateur
 */
public class ConsultationOfrreFragment extends CallbackFragment {


    View rootview;
    private boolean chargee = false;
    ExpandableListView lv;
    private final ArrayList<Offre> listeOffres=new ArrayList<Offre>();
    private final HashMap<String,String> listeAdressesDepartOffre=new HashMap<String,String>();
    private final HashMap<String,String> listeAdressesArriveeOffre=new HashMap<String,String>();

    private final HashMap<String,List<Reservation>> listeReservationsOffre=new HashMap<String,List<Reservation>>();
    private int ParentClickStatus=-1;

    String [] tabDepart;
    String [] tabDestination;

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
     rootview = inflater.inflate(R.layout.activity_consultation_offre, container, false);


     return rootview;
     }


     @Override
     public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         lv = (ExpandableListView) view.findViewById(R.id.expListView);
         Resources res = this.getResources();
         Drawable devider = res.getDrawable(R.drawable.line);
         lv.setGroupIndicator(null);
         lv.setDivider(devider);
         lv.setChildDivider(devider);
         lv.setDividerHeight(1);

         registerForContextMenu(lv);

         ParseQuery<Offre> query = ParseQuery.getQuery("Offre");

         query.whereEqualTo("userOffre", ParseUser.getCurrentUser());
         query.findInBackground(new FindCallback<Offre>() {
             @Override
             public void done(List<Offre> offres, ParseException e) {
                 if (e == null) {
                     tabDepart = new String[offres.size()];
                     tabDestination = new String[offres.size()];
                     listeOffres.addAll(offres);

                     for (Offre offreTrouvee : offres) {

                         ParseQuery<Reservation> queryReservation = ParseQuery.getQuery("Reservation");
                         queryReservation.whereEqualTo("offreSource", offreTrouvee);
                         final Offre oo = offreTrouvee;
                         queryReservation.findInBackground(new FindCallback<Reservation>() {
                             @Override
                             public void done(List<Reservation> reservations, ParseException e) {
                                 listeReservationsOffre.put(oo.getObjectId(),reservations);
                                 System.out.println(oo.getObjectId());
                             }
                         });

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

            final Offre offre = listeOffres.get(groupPosition);
            final  View convertViewLocale = inflater.inflate(R.layout.liste_offres, parentView, false);
            try {
                offre.getTrajet().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Trajet trajetResultat = offre.getTrajet();
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
            ((TextView) convertViewLocale.findViewById(R.id.dateOffre)).setText(sdf.format(offre.getDepart()));
            ((TextView) convertViewLocale.findViewById(R.id.nbPropositions)).setText(String.valueOf(offre.getNbreProposition()));


            class TaskDeparts extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String[] params) {
                    return getRoadName(params);
                }

                protected void onPostExecute(String result) {
                    chargee = true;
                    ((TextView) convertViewLocale.findViewById(R.id.adDepart)).setText(result);
                    listeAdressesDepartOffre.put(offre.getObjectId(),result);

                }

            }

            class TaskDestinations extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String[] params) {
                    return getRoadName(params);
                }

                protected void onPostExecute(String result) {
                    ((TextView) convertViewLocale.findViewById(R.id.adDestination)).setText(result);
                    listeAdressesArriveeOffre.put(offre.getObjectId(),result);

                }

            }
            if(!chargee){
                new TaskDeparts().execute(String.valueOf(lattDep),String.valueOf(longDep));
                new TaskDestinations().execute(String.valueOf(lattArr),String.valueOf(longArr));
            }
            else{
                ((TextView) convertViewLocale.findViewById(R.id.adDepart)).setText(listeAdressesDepartOffre.get(offre.getObjectId()));
                ((TextView) convertViewLocale.findViewById(R.id.adDestination)).setText(listeAdressesArriveeOffre.get(offre.getObjectId()));
            }



            return convertViewLocale;
        }

        // This Function used to inflate child rows view
        @Override
        public View getChildView(int groupPosition,  int childPosition, boolean isLastChild,
                                 View convertView,  ViewGroup parentView){


            final Offre offreSelectionnee = listeOffres.get(groupPosition);
            List<Reservation> listeRes = listeReservationsOffre.get(offreSelectionnee.getObjectId());

            Reservation res = listeRes.get(childPosition);

            convertView = inflater.inflate(R.layout.reservation_row, parentView, false);

            try {
                res.getUserDemandeur().fetchIfNeeded();

            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            ((TextView) convertView.findViewById(R.id.pseudoDemandeur)).setText(res.getUserDemandeur().getUsername().toString());
            ParseUser  us = res.getUserDemandeur();
            Date dateNaissance = us.getDate("birthday");
            System.out.println("dateNaissance demandeur =="+dateNaissance);
           int age = getYears(dateNaissance);

            ((TextView) convertView.findViewById(R.id.age)).setText(age + " ans");
          //  ((TextView) convertView.findViewById(R.id.statutReservation)).setText(res.getReservationStatut().toString());
            return convertView;

        }

        public  int getYears(Date d)
        {
            Calendar curr = Calendar.getInstance();
            Calendar birth = Calendar.getInstance();
            birth.setTime(d);
            int age = curr.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            curr.add(Calendar.YEAR,-age);
            System.out.println("curr demandeur =="+curr);
            System.out.println("birth demandeur =="+birth);
            if(birth.after(curr))
            {
                age = age - 1;
            }
            return age;
        }



        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            final Offre offreSelectionnee = listeOffres.get(groupPosition);
            List<Reservation> listeRes = listeReservationsOffre.get(offreSelectionnee.getObjectId());
            return listeRes.get(childPosition);
        }

        //Call when child row clicked
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            final Offre offreSelectionnee = listeOffres.get(groupPosition);
            List<Reservation> listeRes = listeReservationsOffre.get(offreSelectionnee.getObjectId());
            return childPosition;

        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            final Offre offreSelectionnee = listeOffres.get(groupPosition);
            List<Reservation> listeRes = listeReservationsOffre.get(offreSelectionnee.getObjectId());
            return listeRes.size();
        }


        @Override
        public Object getGroup(int groupPosition)
        {
            Log.i("Offre", groupPosition + "=  getGroup ");
            return listeOffres.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return listeOffres.size();
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
            return ((listeOffres == null) || listeOffres.isEmpty());
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
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
                sbRetour.append(oneObject.getString("formatted_address"));
                System.out.println(sbRetour.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sbRetour.toString();

    }
}