package com.carpool.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import com.carpool.model.User;
import android.content.Intent;

import org.w3c.dom.Text;

/**
 * Created by Bart on 2015-03-07.
 */
public class ProfilActivity extends Fragment {

View rootview;
    @Nullable

    TextView TextViewPseudo;
    TextView TextViewPrenom;
    TextView TextviewNom;
    TextView TextViewdate_naissance;
    TextView TextViewsexe;
    TextView TextViewcourriel;
    Button btn_deconnexion;
    Button btn_modifInfos;
    boolean deconOK = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.profil_layout,container,false);


    //protected void onCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
        //setContentView(R.layout.profil_layout);



        btn_deconnexion = (Button)rootview.findViewById(R.id.deconnexion);
        btn_modifInfos = (Button)rootview.findViewById(R.id.modifier_infoProfil);

        final Intent newActivity;

        btn_deconnexion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                ParseUser.getCurrentUser().logOut();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser == null) {
                    Log.d("succes", "Vous avez ete deconnecte");
                    deconOK = true;

                    //finish();
                }
                else {
                    Log.d("echec", "Vous etes encore connecte");
                    deconOK = false;
                }
            }
        });

        if (deconOK) {
            Intent intent = new Intent(rootview.getContext() , LoginActivity.class);
            startActivity(intent);
            //rootview.getContext().fifinish();
        }

        // Ici je met mon code
       User utilisateur = new User();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null)
        {
            TextViewcourriel = (TextView)rootview.findViewById(R.id.txt_courriel_userProfil);
            TextViewdate_naissance = (TextView)rootview.findViewById(R.id.txt_date_naiss_userProfil);
            TextViewsexe = (TextView)rootview.findViewById(R.id.txt_sexe_userProfil);
            TextviewNom = (TextView)rootview.findViewById(R.id.txt_nom_userProfil);
            TextViewPrenom = (TextView)rootview.findViewById(R.id.txt_prenom_userProfil);
            TextViewPseudo = (TextView)rootview.findViewById(R.id.txt_pseudo_userProfil);


            TextViewPseudo.setText(currentUser.getUsername());
            TextviewNom.setText(currentUser.getString("firstname"));
            TextViewPrenom.setText(currentUser.getString("lastname"));
            TextViewdate_naissance.setText(currentUser.getDate("birthday").getYear()+"/"
                    +currentUser.getDate("birthday").getMonth()
                    +"/"+currentUser.getDate("birthday").getDay());
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
