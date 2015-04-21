package com.carpool.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.carpool.R;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.ui.fragments.ProfilConducteurFragment;
import java.io.*;
import com.parse.ParseUser;

/**
 * Created by akouemodarisca on 21/04/15.
 */
public class ProfilConducteurActivity extends AccueilActivity implements
        CallbackFragment.Callbacks {

    public static  ParseUser conducteurUser;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle bundle = new Bundle();


        //Intent myIntent = getIntent();
        //conducteurUser = (ParseUser) myIntent.getParcelableExtra("conducteur");

        /*
        Bundle bundleOffre = myIntent.getExtras();
        try {
            conducteurUser = (ParseUser)bytes2Object(bundleOffre.getByteArray("conducteurUser"));
            Log.d("bundle", "in ProfilConducteurActivity");
        }catch (ClassNotFoundException e){

        }catch (IOException a){

        }*/

        bundle.putString("Title", "ProfilConducteurActivity");
        ProfilConducteurFragment of = new ProfilConducteurFragment();
        of.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.session_fragment, of).commit();
    }


    public Object bytes2Object( byte raw[] )
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
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


            return AccueilActivity.NAVDRAWER_ITEM_RECHERCHE;

    }

    protected int getSelfNavDrawerItem() {

            return NAVDRAWER_ITEM_RECHERCHE;
    }

}
