package com.carpool.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.ui.adapters.SampleAdapter;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.design.SlidingTabLayout;

import java.util.ArrayList;

public class RechercheResultatActivity extends AccueilActivity implements
        CallbackFragment.Callbacks  {


    public static ArrayList<String> listOffreId;
    public static Offre offreSelectionne;

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
