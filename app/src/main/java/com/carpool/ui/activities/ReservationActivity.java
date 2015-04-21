package com.carpool.ui.activities;

import android.os.Bundle;

import com.carpool.R;
import com.carpool.ui.adapters.SampleAdapterReservation;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.fragments.ConsultationOfrreFragment;
import com.carpool.ui.fragments.OffreFragment;
import com.carpool.ui.fragments.ReservationFragment;
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
/**
 * Created by sachin on 17/1/15.
 */
public class ReservationActivity {

        @Override
        public void onItemSelected(long id) { }

        @Override
        public int getSelectedFragment() {
        return AccueilActivity.NAVDRAWER_ITEM_MES_RESERVATIONS;
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

        }

        private PagerAdapter buildAdapter() {
        return(new SampleAdapterReservation(this, getSupportFragmentManager()));
        }


        @Override
        protected void onPostCreate(Bundle savedInstanceState) {

            super.onPostCreate(savedInstanceState);
        }

        //@Override
        protected void onActionBarAutoShowOrHide(boolean shown) {
          //      super.onActionBarAutoShowOrHide(shown);
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
            return AccueilActivity.NAVDRAWER_ITEM_MES_RESERVATIONS;
        }

}
