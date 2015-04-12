package com.carpool.ui.fragments;

import android.graphics.Color;
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

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.ui.activities.RechercheResultatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.carpool.utils.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

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
                                        if (offres != null)
                                            Log.d("Activity1Fragment",String.valueOf(offres.size()));
                                        else
                                            Log.d("Activity1Fragment","liste vide");

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

    @Override
    public void onResume() {
        super.onResume();
        switch(position_) {
            case 1:


                if (map == null) {
                map = fragment.getMap();
                map.addMarker(new MarkerOptions().position(new LatLng(12, -12)));
                int i = 0;//test pour provoquer l'addition des changement à la branche
                break;
            }
        }
    }
}