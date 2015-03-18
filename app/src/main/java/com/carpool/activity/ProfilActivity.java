package com.carpool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.carpool.activity.LoginActivity.UserLoginTask;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import com.carpool.model.User;
import android.content.Intent;
import java.util.Date;
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
    UserSignOutTask mDeconnexionTask;
    Activity activite ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.profil_layout, container, false);


        //protected void onCreate(Bundle savedInstanceState) {
        //  super.onCreate(savedInstanceState);
        //setContentView(R.layout.profil_layout);


        btn_deconnexion = (Button) rootview.findViewById(R.id.deconnexion);
        btn_modifInfos = (Button) rootview.findViewById(R.id.modifier_infoProfil);

        final Intent newActivity;


        // Ici je met mon code
        User utilisateur = new User();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            TextViewcourriel = (TextView) rootview.findViewById(R.id.txt_courriel_userProfil);
            TextViewdate_naissance = (TextView) rootview.findViewById(R.id.txt_date_naiss_userProfil);
            TextViewsexe = (TextView) rootview.findViewById(R.id.txt_sexe_userProfil);
            TextviewNom = (TextView) rootview.findViewById(R.id.txt_nom_userProfil);
            TextViewPrenom = (TextView) rootview.findViewById(R.id.txt_prenom_userProfil);
            TextViewPseudo = (TextView) rootview.findViewById(R.id.txt_pseudo_userProfil);

            Date date_naiss = currentUser.getDate("birthday");

            TextViewPseudo.setText(currentUser.getUsername());
            TextviewNom.setText(currentUser.getString("firstname"));
            TextViewPrenom.setText(currentUser.getString("lastname"));
            TextViewdate_naissance.setText(String.valueOf(currentUser.getDate("birthday").getDay()));
            TextViewdate_naissance.append(" "+String.valueOf(currentUser.getDate("birthday").getMonth()));

            Log.d("valeur du mois", String.valueOf(currentUser.getDate("birthday").getMonth()));
            switch(date_naiss.getMonth())
            {
                case 0: TextViewdate_naissance.append(" Aucun mois ");
                    break;
                case 1: TextViewdate_naissance.append(" Janvier ");
                    break;
                case 2: TextViewdate_naissance.append(" Fevrier ");
                    break;
                case 3: TextViewdate_naissance.append(" Mars ");
                    break;
                case 4: TextViewdate_naissance.append(" Avril ");
                    break;
                case 5: TextViewdate_naissance.append(" Mai ");
                    break;
                case 6: TextViewdate_naissance.append(" Juin ");
                    break;
                case 7: TextViewdate_naissance.append(" Juillet ");
                    break;
                case 8: TextViewdate_naissance.append(" Aout ");
                    break;
                case 9: TextViewdate_naissance.append(" Septembre ");
                    break;
                case 10: TextViewdate_naissance.append(" Octobre ");
                    break;
                case 11: TextViewdate_naissance.append(" Novembre ");
                    break;
                case 12: TextViewdate_naissance.append(" Decembre ");
                    break;
            }
            TextViewdate_naissance.append(" "+String.valueOf(date_naiss.getYear()));

            TextViewsexe.setText(currentUser.getString("gender"));
            TextViewcourriel.setText(currentUser.getString("email"));

            Log.d("trouve", "un utilisateur a ete trouve" + currentUser.getObjectId());
        } else {
            Log.d("faux", "un utilisateur na pas ete trouve");
        }

        activite = this.getActivity();
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                mDeconnexionTask = new UserSignOutTask(currentUser.getUsername(), currentUser.getString("password"), activite);
                mDeconnexionTask.cancel(true);
            }
        });


        if (deconOK) {
            // ActionBarActivity actionBar = new ActionBarActivity();

            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(intent);
            this.getActivity().finish();
            //rootview.getContext().fifinish();
        }

        return rootview;
    }


    public class UserSignOutTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;
        private Activity activite;

        UserSignOutTask(String username_, String password_, Activity activite_) {
            username = username_;
            password = password_;
            activite = activite_;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {
            Log.d("cancel", "dans le cancel");
            ParseUser.logOut();
            mDeconnexionTask = null;
            //showProgress(false);

            Intent newActivity = new Intent(activite, LoginActivity.class);
            startActivity(newActivity);
            activite.finish();

        }

    }
}













