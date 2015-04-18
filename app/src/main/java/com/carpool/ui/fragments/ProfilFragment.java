package com.carpool.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.carpool.R;
import com.carpool.ui.activities.ModificationProfilActivity;
import com.carpool.ui.activities.ProfilLoginActivity;
import com.carpool.ui.design.CallbackFragment;
import com.parse.ParseUser;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import com.carpool.utils.*;
/**
 * classe qui permet de recuperer les informations de l'utilisateur qui se connecte dans la DB et
 * qui les affiche sur le profil de l'utilisateur
 */
public class ProfilFragment extends CallbackFragment {

    View rootview;


    TextView TextViewPseudo;
    TextView TextViewPrenom;
    TextView TextviewNom;
    TextView TextViewdate_naissance;
    TextView TextViewsexe;
    TextView TextViewcourriel;
    UserSignOutTask mDeconnexionTask;


    /**
     * The fragment's current callback object.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }

        @Override
        public int getSelectedFragment() {
            return 0;
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_profil_layout, container, false);

        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
                "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );

        ParseUser currentUser = ParseUser.getCurrentUser();
        int age = 0;

        if (currentUser != null) {

            TextViewcourriel = (TextView) rootview.findViewById(R.id.txt_courriel_userProfil);
            TextViewdate_naissance = (TextView) rootview.findViewById(R.id.txt_date_naiss_userProfil);
            TextViewsexe = (TextView) rootview.findViewById(R.id.txt_sexe_userProfil);
            TextviewNom = (TextView) rootview.findViewById(R.id.txt_nom_userProfil);
            TextViewPrenom = (TextView) rootview.findViewById(R.id.txt_prenom_userProfil);
            //TextViewPseudo = (TextView) rootview.findViewById(R.id.txt_pseudo_userProfil);

            Date date_naiss = currentUser.getDate("birthday");

            //TextViewPseudo.setText(currentUser.getUsername());
            TextviewNom.setText(currentUser.getString("firstname"));
            TextViewPrenom.setText(currentUser.getString("lastname"));
            int jour_naiss = getDayOfMonth(date_naiss);
            TextViewdate_naissance.setText(String.valueOf(jour_naiss));

            String[] Months = new DateFormatSymbols().getMonths();
            TextViewdate_naissance.append(" "+Months[date_naiss.getMonth()]+" ");

            int date_n = date_naiss.getYear()+1900;
            TextViewdate_naissance.append(String.valueOf(date_n));

            TextViewsexe.setText(currentUser.getString("gender"));
            TextViewcourriel.setText(currentUser.getString("email"));

            Log.d("trouve", "un utilisateur a ete trouve" + currentUser.getObjectId());

            age = calculAge(date_n);

        } else {
            Log.d("faux", "un utilisateur na pas ete trouve");
        }

        // indiquer pseudo
        TextView indic_pseudo = (TextView)rootview.findViewById(R.id.indic_pseudo);
        indic_pseudo.setText(currentUser.getUsername());

        //indiquer age

        TextView indic_age = (TextView)rootview.findViewById(R.id.indic_age);
        indic_age.setText(age+" ans");

        // camera
        Button camera = (Button)rootview.findViewById(R.id.indic_camera);
        camera.setTypeface(font);


        FloatingActionButton fab = (FloatingActionButton)rootview.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        fab.setTypeface(font);

        fab.setOnClickListener( new View.OnClickListener(){


            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ModificationProfilActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();

            }
        });

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

            Intent newActivity = new Intent(activite, ProfilLoginActivity.class);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    public int calculAge(int year)
    {
        Calendar cal = Calendar.getInstance();

        int current_year = cal.get(Calendar.YEAR);
        return current_year - year;
    }
}