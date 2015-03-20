package com.carpool.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;

public class RechercheActivity extends Fragment{

    Context context; //Declare the variable context

View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.recherche_layout,container,false);

       context = rootview.getContext(); // Assign your rootView to context
       //super.onCreate(savedInstanceState);
        //setcontentview(R.layout.recherche_layout);

       //Pass the context and the Activity class you need to open from the Fragment Class, to the Intent
        Intent intent = new Intent(context, Adresse.class);
        startActivity(intent);
        return rootview;
    }




}

