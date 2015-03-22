package com.carpool.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.carpool.activity.LoginActivity.UserLoginTask;

import com.carpool.utils.FloatingActionButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import com.carpool.model.User;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.Text;
import java.util.Calendar;

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.profil_layout, container, false);

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);*/

        //protected void onCreate(Bundle savedInstanceState) {
        //  super.onCreate(savedInstanceState);
        //setContentView(R.layout.profil_layout);


//        btn_deconnexion = (Button) findViewById(R.id.deconnexion);
  //      btn_modifInfos = (Button) findViewById(R.id.modifier_infoProfil);


        //btn_deconnexion = (Button)rootview.findViewById(R.id.deconnexion);
        //btn_modifInfos = (Button) rootview.findViewById(R.id.modifier_infoProfil);


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

            /*TextViewcourriel = (TextView) findViewById(R.id.txt_courriel_userProfil);
            TextViewdate_naissance = (TextView) findViewById(R.id.txt_date_naiss_userProfil);
            TextViewsexe = (TextView) findViewById(R.id.txt_sexe_userProfil);
            TextviewNom = (TextView) findViewById(R.id.txt_nom_userProfil);
            TextViewPrenom = (TextView) findViewById(R.id.txt_prenom_userProfil);
            TextViewPseudo = (TextView) findViewById(R.id.txt_pseudo_userProfil);*/

            Date date_naiss = currentUser.getDate("birthday");

            TextViewPseudo.setText(currentUser.getUsername());
            TextviewNom.setText(currentUser.getString("firstname"));
            TextViewPrenom.setText(currentUser.getString("lastname"));
            TextViewdate_naissance.setText(String.valueOf(getDayOfMonth(date_naiss)));
           // TextViewdate_naissance.append(" "+String.valueOf(currentUser.getDate("birthday").getMonth()));

            /*
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            try {
                d = sdf.parse(date_naiss.toString());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }


            Log.d("valeur du jour modif", String.valueOf(d.getDay()));
            Log.d("valeur du jour ancien", String.valueOf(date_naiss.getDay()));
            Log.d("getDayofmonth", String.valueOf(getDayOfMonth(date_naiss)));

            Log.d("valeur du mois ancien", String.valueOf(date_naiss.getMonth()));
            Log.d("valeur du mois modifie", String.valueOf(d.getMonth()));

            Log.d("valeur an ancien", String.valueOf(date_naiss.getYear()));
            Log.d("valeur an modifie", String.valueOf(d.getYear()));*/

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

        /*btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                mDeconnexionTask = new UserSignOutTask(currentUser.getUsername(), currentUser.getString("password"), activite);
                mDeconnexionTask.cancel(true);
            }
        });*/


        if (deconOK) {
            // ActionBarActivity actionBar = new ActionBarActivity();


            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(intent);
            this.getActivity().finish();
            /*//rootview.getContext().fifinish(); */

            /*Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();*/

        }

        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
                "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );


        FloatingActionButton fab = (FloatingActionButton)rootview.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        //fab.setTypeface(font);
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
            //showProgress(false);

            Intent newActivity = new Intent(activite, LoginActivity.class);
            startActivity(newActivity);
            activite.finish();

        }
    }

    public static int getDayOfMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }


}













