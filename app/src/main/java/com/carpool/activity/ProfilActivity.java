package com.carpool.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import android.util.Log;

/**
 * Created by Bart on 2015-03-07.
 */
public class ProfilActivity extends Fragment{
View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.profil_layout,container,false);

        // Ici je met mon code

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
            Log.d("trouve", "un utilisateur a ete trouve" + currentUser.getObjectId());
        }
        else
        {
            Log.d("faux", "un utilisateur na pas ete trouve");
        }






        return rootview;






    }
}
