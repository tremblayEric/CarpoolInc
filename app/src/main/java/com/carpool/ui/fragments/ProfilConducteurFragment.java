package com.carpool.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carpool.R;
import com.carpool.ui.activities.ModificationProfilActivity;
import com.carpool.ui.activities.ProfilConducteurActivity;
import com.carpool.ui.activities.ProfilLoginActivity;
import com.carpool.ui.design.CallbackFragment;
import com.carpool.utils.FloatingActionButton;
import com.carpool.utils.MyResultSearchListAdapter;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

/**
 * classe qui permet de recuperer les informations de l'utilisateur qui se connecte dans la DB et
 * qui les affiche sur le profil de l'utilisateur
 */
public class ProfilConducteurFragment extends CallbackFragment {//implements SurfaceHolder.Callback  {

    View rootview;



    // gestion de la camera
    private static final int CAMERA_PIC_REQUEST = 001;
    String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    Bitmap thumbnail;
    TextView TextViewcourriel;
    TextView TextViewdate_naissance;
    TextView TextViewsexe;
    TextView TextviewNom;
    TextView TextViewPrenom;
    TextView indic_pseudo;
    TextView indic_age;
    ImageView imagePhoto;
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
        rootview = inflater.inflate(R.layout.profil_conducteur, container, false);

        ParseUser conducteurUser = MyResultSearchListAdapter.userInfoConduct;// ProfilConducteurActivity.conducteurUser;
        int age = 0;

        if (conducteurUser != null) {
            TextViewcourriel = (TextView) rootview.findViewById(R.id.txt_courriel_userProfil_c);

            TextViewsexe = (TextView) rootview.findViewById(R.id.txt_sexe_userProfil_c);
            TextviewNom = (TextView) rootview.findViewById(R.id.txt_nom_userProfil_c);
            TextViewPrenom = (TextView) rootview.findViewById(R.id.txt_prenom_userProfil_c);
            indic_pseudo = (TextView)rootview.findViewById(R.id.indic_pseudo_c);
            indic_age = (TextView)rootview.findViewById(R.id.indic_age_c);
            imagePhoto = (ImageView)rootview.findViewById(R.id.imageView1_c);
        /*
        imagePhoto = (ImageView)rootview.findViewById(R.id.imageView1);
        //TextViewPseudo = (TextView) rootview.findViewById(R.id.txt_pseudo_userProfil);*/

            Date date_naiss = conducteurUser.getDate("birthday");

            //TextViewPseudo.setText(currentUser.getUsername());
            TextviewNom.setText(conducteurUser.getString("firstname"));
            TextViewPrenom.setText(conducteurUser.getString("lastname"));
            int jour_naiss = getDayOfMonth(date_naiss);
            int date_n = date_naiss.getYear() + 1900;
            TextViewsexe.setText(conducteurUser.getString("gender"));
            TextViewcourriel.setText(conducteurUser.getString("email"));

            Log.d("trouve", "un utilisateur a ete trouve" + conducteurUser.getObjectId());

            age = calculAge(date_n);

            indic_pseudo.setText(conducteurUser.getUsername());

            //indiquer age
            indic_age.setText(age+" ans");
        }
        // telecharger l'image et la mettre dans le circleView


        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "_User");

            query.getInBackground(conducteurUser.getObjectId(),
                    new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, com.parse.ParseException e) {
                            ParseFile fileObject = (ParseFile) object.get("imageFile");
                            if (fileObject != null) {
                                fileObject.getDataInBackground(new GetDataCallback() {

                                    @Override
                                    public void done(byte[] data, com.parse.ParseException e) {
                                        if (e == null) {
                                            Log.d("test", "We've got data in data.");
                                            // Decode the Byte[] into, Bitmap
                                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            // Set the Bitmap into the, ImageView
                                            imagePhoto.setImageBitmap(bmp);
                                        } else {
                                            Log.d("test", "There was a problem downloading the data.");
                                        }
                                    }
                                });
                            }
                        }
                    });
        }
        catch (Exception e)
        {

        }




        return rootview;

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

    public static int getDayOfMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }


}