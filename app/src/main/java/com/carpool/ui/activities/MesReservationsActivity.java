package com.carpool.ui.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.carpool.R;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.fragments.ConsultationOfrreFragment;
import com.carpool.ui.fragments.MesReservationsFragment;

public class MesReservationsActivity extends AccueilActivity implements
        CallbackFragment.Callbacks {



    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle bundle = new Bundle();

        bundle.putString("Title", "CARPOOL INC.");

        MesReservationsFragment of = new MesReservationsFragment();
        of.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.session_fragment, of).commit();

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
        return AccueilActivity.NAVDRAWER_ITEM_MES_RESERVATIONS;
    }
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_MES_RESERVATIONS;
    }

}
