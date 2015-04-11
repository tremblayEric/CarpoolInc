package com.carpool.ui.activities;

import android.os.Bundle;

import com.carpool.R;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.fragments.LoginFragment;
import com.carpool.ui.fragments.ProfilFragment;
import com.parse.ParseUser;

/**
 * Created by sachin on 17/1/15.
 */
public class ProfilLoginActivity extends AccueilActivity implements
        CallbackFragment.Callbacks {

    ParseUser current = ParseUser.getCurrentUser();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle bundle = new Bundle();


        if (current != null) {
            bundle.putString("Title", "ProfilActivity");
            ProfilFragment of = new ProfilFragment();
            of.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.session_fragment, of).commit();
        }
        else
        {
            bundle.putString("Title", "LogInACtivity");
            LoginFragment of = new LoginFragment();
            of.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.session_fragment, of).commit();
        }

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

        if (current != null)
        return AccueilActivity.NAVDRAWER_ITEM_PROFIL_ACTIVITY1;
        else
            return AccueilActivity.NAVDRAWER_ITEM_CONNEXION;
    }

    protected int getSelfNavDrawerItem() {

        if (current != null)
        return NAVDRAWER_ITEM_PROFIL_ACTIVITY1;
        else
            return NAVDRAWER_ITEM_CONNEXION;
    }

}
