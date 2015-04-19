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
import com.carpool.utils.MyResultSearchListAdapter;
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
        Intent myIntent = getIntent();
        Bundle bundleOffre = myIntent.getExtras();
        listOffreId = bundleOffre.getStringArrayList("offres");
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
