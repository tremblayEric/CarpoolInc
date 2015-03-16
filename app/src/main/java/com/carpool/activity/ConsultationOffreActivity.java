package com.carpool.activity;

    import android.content.Context;
    import android.content.res.Resources;
    import android.graphics.drawable.Drawable;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseExpandableListAdapter;
    import android.widget.ExpandableListView;
    import android.widget.TextView;

    import com.carpool.model.Offre;
    import com.carpool.model.Trajet;
    import com.parse.FindCallback;
    import com.parse.ParseException;
    import com.parse.ParseObject;
    import com.parse.ParseQuery;

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
    import java.util.List;

/**
     * Created by Bart on 2015-03-07.
     */
    public class ConsultationOffreActivity extends Fragment {

        View rootview;
        ExpandableListView lv;
        private final ArrayList<Offre> listeOffres=new ArrayList<Offre>();
        private int ParentClickStatus=-1;

        TextView adresseDepartTextView;
        TextView adresseDestinationTextView;
        String [] tabDepart;
        String [] tabDestination;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootview = inflater.inflate(R.layout.activity_consultation_offre,container,false);
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


            ParseQuery<Offre> query = ParseQuery.getQuery("Offre");
            //query.whereEqualTo("playerName", "Dan Stemkoski");
            query.findInBackground(new FindCallback<Offre>() {
                @Override
                public void done(List<Offre> offres, ParseException e) {
                    if (e == null) {
                        tabDepart = new String[offres.size()];
                        tabDestination = new String[offres.size()];
                        listeOffres.addAll(offres);
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

                int lattDep = trajetResultat.getPositionDepart().getLatitude();
                int longDep = trajetResultat.getPositionDepart().getLongitude();

                int lattArr = trajetResultat.getPositionArrive().getLatitude();
                int longArr = trajetResultat.getPositionArrive().getLongitude();


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ((TextView) convertViewLocale.findViewById(R.id.dateOffre)).setText(sdf.format(offre.getDepart()));
                ((TextView) convertViewLocale.findViewById(R.id.nbPropositions)).setText(String.valueOf(offre.getNbreProposition()));

                String resultat="";




                class TaskDeparts extends AsyncTask<String, String, String> {

                    @Override
                    protected String doInBackground(String[] params) {
                        return getRoadName(params);
                    }

                    protected void onPostExecute(String result) {
                        ((TextView) convertViewLocale.findViewById(R.id.adDepart)).setText(result);

                    }

                }


                class TaskDestinations extends AsyncTask<String, String, String> {

                    @Override
                    protected String doInBackground(String[] params) {
                        return getRoadName(params);
                    }

                    protected void onPostExecute(String result) {
                        ((TextView) convertViewLocale.findViewById(R.id.adDestination)).setText(result);

                    }

                }
                new TaskDeparts().execute(String.valueOf(lattDep),String.valueOf(longDep));
                new TaskDestinations().execute(String.valueOf(lattArr),String.valueOf(longArr));


                return convertViewLocale;
            }

            // This Function used to inflate child rows view
            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                     View convertView, ViewGroup parentView)
            {
                return null;
            }


            @Override
            public Object getChild(int groupPosition, int childPosition)
            {
                //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
                return null ; //listeOffres.get(groupPosition).getChildren().get(childPosition);
            }

            //Call when child row clicked
            @Override
            public long getChildId(int groupPosition, int childPosition)
            {
                return 0;
            }

            @Override
            public int getChildrenCount(int groupPosition)
            {
                int size=0;
                return size;
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
                Log.i("Offre", groupPosition+"=  getGroupId "+ParentClickStatus);
                ParentClickStatus=groupPosition;
                if(ParentClickStatus==0)
                    ParentClickStatus=-1;

                return groupPosition;
            }

            @Override
            public void notifyDataSetChanged()
            {
                // Refresh List rows
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

            String roadName = "";
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
