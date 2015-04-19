package com.carpool.ui.activities;

import android.os.Bundle;

import com.carpool.R;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.fragments.ConsultationOfrreFragment;

public class ConsultationOffreActivity  extends AccueilActivity implements
        CallbackFragment.Callbacks {

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle bundle = new Bundle();

        bundle.putString("Title", "ConsultationOffreActivity");
        if(getIntent().getExtras() != null && getIntent().getExtras().get("offreId") != null) {
            bundle.putString("offreIdNotifie", getIntent().getExtras().get("offreId").toString());
        }

        ConsultationOfrreFragment of = new ConsultationOfrreFragment();
        of.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
        .add(R.id.session_fragment, of).commit();

       }

        @Override
        protected int getLayoutResource() {
            return R.layout.activity_main;
        }

        @Override
        public void onItemSelected(long l) {

        }

        @Override
        public int getSelectedFragment() {
         return AccueilActivity.NAVDRAWER_ITEM_MES_ANNONCE;
        }

        protected int getSelfNavDrawerItem() {
            return NAVDRAWER_ITEM_MES_ANNONCE;
        }

}