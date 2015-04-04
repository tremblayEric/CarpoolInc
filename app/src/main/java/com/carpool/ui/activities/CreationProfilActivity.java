package com.carpool.ui.activities;

import android.os.Bundle;

import com.carpool.R;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.fragments.CreationProfilFragment;

/**
 * Created by sachin on 17/1/15.
 */
public class CreationProfilActivity extends AccueilActivity implements
        CallbackFragment.Callbacks {


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("Title", "CreationProfilActivity");

        CreationProfilFragment of = new CreationProfilFragment();
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
        return AccueilActivity.NAVDRAWER_ITEM_CONNEXION;
    }

    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_CONNEXION;
    }


}