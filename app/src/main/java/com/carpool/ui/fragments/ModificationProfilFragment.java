package com.carpool.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.carpool.R;

/***
 * Classe qui initialise un formulaire pour que l'utilisateur puisse ajouter une offre de covoiturage.
 * L'annonce est ajoutée à la base de donnée
 */
public class ModificationProfilFragment extends Fragment {

    View rootview;
    Button btn_modif_profil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_modif_profil, container, false);

        btn_modif_profil = (Button)rootview.findViewById(R.id.modif_info_profil);
        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
                "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );
        btn_modif_profil.append("    MODIFIER");
        btn_modif_profil.setTypeface(font);

        return rootview;
    }


}