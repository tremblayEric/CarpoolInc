package mgl7130.tiroir;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

/**
 * Created by Bart on 2015-03-07.
 */
public class Recherche extends Fragment{
View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.recherche_layout,container,false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recherche_layout);


        return rootview;
    }
}
