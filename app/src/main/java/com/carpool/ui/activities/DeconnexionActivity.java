package com.carpool.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.carpool.R;
import com.carpool.ui.design.CallbackFragment;
import com.parse.ParseUser;

/**
 * Created by sachin on 17/1/15.
 */
public class DeconnexionActivity extends AccueilActivity implements
        CallbackFragment.Callbacks  {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putString("Title", "DeconnexionActivity");
        bundle.putString("deconnexion", "OK");

        // Gestion de la deconnexion

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.isAuthenticated()) {
            ParseUser.logOut();

            currentUser=ParseUser.getCurrentUser();
            if(currentUser == null)
                Log.d("deconnexion", "success");
            else
                Log.d("deconnexion", "echec");
        }


        Intent intent = new Intent(this, ProfilLoginActivity.class);
        startActivity(intent);
        finish();

        /*
        LoginFragment of = new LoginFragment();
        of.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.session_fragment, of).commit();*/

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
        return AccueilActivity.NAVDRAWER_ITEM_DECONNEXION;
    }

    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_DECONNEXION;
    }
}
