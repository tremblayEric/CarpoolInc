package com.carpool.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import android.util.Log;
import android.widget.TextView;
import com.carpool.model.User;

import org.w3c.dom.Text;

/**
 * Created by Bart on 2015-03-07.
 */
public class ProfilActivity extends Fragment{
View rootview;
    @Nullable

    TextView TextViewPseudo;
    TextView TextViewPrenom;
    TextView TextviewNom;
    TextView TextViewdate_naissance;
    TextView TextViewsexe;
    TextView TextViewcourriel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.profil_layout,container,false);

        // Ici je met mon code
       User utilisateur = new User();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
            TextViewcourriel = (TextView)rootview.findViewById(R.id.txt_courriel_userProfil);
            TextViewdate_naissance = (TextView)rootview.findViewById(R.id.txt_date_naiss_userProfil);
            TextViewsexe = (TextView) rootview.findViewById(R.id.txt_sexe_userProfil);
            TextviewNom = (TextView)rootview.findViewById(R.id.txt_nom_userProfil);
            TextViewPrenom = (TextView)rootview.findViewById(R.id.txt_prenom_userProfil);
            TextViewPseudo = (TextView) rootview.findViewById(R.id.txt_pseudo_userProfil);

            TextViewPseudo.setText(currentUser.getUsername());
            TextviewNom.setText(currentUser.getString("firstname"));
            TextViewPrenom.setText(currentUser.getString("lastname"));
            TextViewdate_naissance.setText(currentUser.getString("birthday"));
            TextViewsexe.setText(currentUser.getString("gender"));
            TextViewcourriel.setText(currentUser.getString("email"));



            Log.d("trouve", "un utilisateur a ete trouve" + currentUser.getObjectId());
        }
        else
        {



            Log.d("faux", "un utilisateur na pas ete trouve");
        }






        return rootview;






    }
}
