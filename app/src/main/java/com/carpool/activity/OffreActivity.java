package com.carpool.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by Bart on 2015-03-07.
 */
public class OffreActivity extends Fragment{
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.offre_layout,container,false);

        ((TimePicker)rootview.findViewById(R.id.etBetweenStart)).setIs24HourView(true);

        Button submitOffer = (Button)(rootview.findViewById(R.id.btnSubmitOffer));
        submitOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootview;
    }
}
