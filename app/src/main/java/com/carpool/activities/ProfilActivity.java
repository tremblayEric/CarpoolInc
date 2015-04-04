package com.carpool.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.carpool.utils.FloatingActionButton;
import com.parse.ParseUser;
import android.util.Log;
import android.widget.TextView;
import java.util.Date;
import java.util.Calendar;

/**
 * classe qui permet de recuperer les informations de l'utilisateur qui se connecte dans la DB et
 * qui les affiche sur le profil de l'utilisateur
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
    boolean deconOK = false;
    UserSignOutTask mDeconnexionTask;
    Activity activite ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.profil_layout, container, false);

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
            TextViewdate_naissance.setText(String.valueOf(getDayOfMonth(date_naiss)));

            switch(date_naiss.getMonth()+1)
            {

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
            TextViewdate_naissance.append(" "+String.valueOf(date_naiss.getYear()+1900));

            TextViewsexe.setText(currentUser.getString("gender"));
            TextViewcourriel.setText(currentUser.getString("email"));

            Log.d("trouve", "un utilisateur a ete trouve" + currentUser.getObjectId());
        } else {
            Log.d("faux", "un utilisateur na pas ete trouve");
        }

        activite = this.getActivity();

        if (deconOK) {

            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(intent);
            this.getActivity().finish();
        }

        FloatingActionButton fab = (FloatingActionButton)rootview.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));

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

            Intent newActivity = new Intent(activite, LoginActivity.class);
            startActivity(newActivity);
            activite.finish();

        }
    }

    /**
     * fonction qui permet d'obtenir le jour du mois
     * @param aDate
     * @return
     */

    public static int getDayOfMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}