package com.carpool.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.carpool.R;
import com.carpool.model.Offre;
import com.carpool.model.User;
import com.carpool.ui.activities.ProfilLoginActivity;
import com.carpool.ui.activities.RechercheActivity;
import com.carpool.ui.design.CallbackFragment;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Classe qui initialise un formulaire pour que l'utilisateur puisse ajouter une offre de covoiturage.
 * L'annonce est ajoutée à la base de donnée
 */
public class ModificationProfilFragment extends Fragment {

    View rootview;
    Button btn_modif_profil;
    DatePickerDialog datePickerDialog;

    // Variables representant les composants de l'ui
    private EditText ancienMdp;
    private  EditText mdp;
    private  EditText confmdp ;
    private  EditText dateNais ;
    private  EditText nom ;
    private  EditText prenom;
    private TextView pseudo;
    private TextView mail;
    private TextView compteCree;
    private RadioButton radioM;
    private RadioButton radioF;


    static String strAncienMdp ;
    static String strMdp ;
    static String strConfMdp ;
    static String strDateNais;
    static String strSex ;
    static String strNom;
    static String strPrenom ;


    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    Toolbar toolbar;

    private CallbackFragment.Callbacks mCallbacks = sDummyCallbacks;
    private static CallbackFragment.Callbacks sDummyCallbacks = new CallbackFragment.Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }

        @Override
        public int getSelectedFragment() {
            return 0;
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_modif_profil, container, false);

        btn_modif_profil = (Button)rootview.findViewById(R.id.modif_info_profil);
        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
                "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );
        btn_modif_profil.append("    MODIFIER");
        btn_modif_profil.setTypeface(font);

        /**
         * Recuperer les valeurs des composants dans des variables
         */
        pseudo = (TextView)rootview.findViewById(R.id.txt_modif_pseudo_userProfil);
        mail = (TextView)rootview.findViewById(R.id.txt_modif_courriel_userProfil);
        ancienMdp  = (EditText) rootview.findViewById(R.id.txt_ancien_mdp);
        mdp = (EditText) rootview.findViewById(R.id.txtMdp_modif);
        confmdp = (EditText) rootview.findViewById(R.id.txtConfMdp_modif);
        dateNais = (EditText) rootview.findViewById(R.id.txt_modif_date_naiss_userProfil);
        nom = (EditText) rootview.findViewById(R.id.txt__modif_nom_userProfil);
        prenom = (EditText) rootview.findViewById(R.id.txt_modif_prenom_userProfil);

        radioM = (RadioButton)rootview.findViewById(R.id.radioM_modif);
        radioF = (RadioButton)rootview.findViewById(R.id.radioF_modif);

        //compteCree = (TextView) rootview.findViewById(R.id.compteCree);
       ((RadioButton)rootview.findViewById(R.id.radioF_modif)).setOnTouchListener(radioButtonListener);
        ((RadioButton)rootview.findViewById(R.id.radioM_modif)).setOnTouchListener(radioButtonListener);
        /**
         * Obtenir l'heure courante du calendrier
         */
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        /**
         * gestion affichage calendrier
         */
        final EditText txtCalendar = (EditText) rootview.findViewById(R.id.txt_modif_date_naiss_userProfil);
        txtCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePickerDialog.show();
                return false;
            }
        });
        btn_modif_profil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                modifierUtilisateur(v);
            }
        });
       // Initialisation des champs




        ParseUser us = ParseUser.getCurrentUser();
        pseudo.setText(us.getString("username"));
        mail.setText(us.getString("email"));
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        dateNais.setText(sdf.format(us.getDate("birthday")));
        nom.setText(us.getString("lastname"));
        prenom.setText(us.getString("firstname"));

        String gender = us.getString("gender");
       if(gender.equals("M")){

           radioM.setChecked(true);
       }
        else{
           radioF.setChecked(true);
       }




        /**
         *  Controle sur l'ancien mdp saisie
         *
         */
   //     strAncienMdp = ancienMdp.getText().toString();

        /**
         *  Controle sur la confirmation de Mdp
         */
        final EditText password1 = (EditText) rootview.findViewById(R.id.txtMdp_modif);
        final EditText password2 = (EditText) rootview.findViewById(R.id.txtConfMdp_modif);

        strMdp = password1.getText().toString();

        /**
         * vérification du mot de passe et de la confirmation de mot de passe
         */
        password2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = password1.getText().toString();
                String strPass2 = password2.getText().toString();
                if (!strPass1.equals(strPass2)) {

                    password2.setError("Confirmation invalide");
                }
                else{
                    password2.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        password1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = password1.getText().toString();
                String strPass2 = password2.getText().toString();
                if (!strPass1.equals(strPass2) && strPass2.length()>0) {
                    password2.setError("Confirmation invalide");
                }
                else{
                    password2.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        /**
         * Controle sur la taille du Mdp
         */
        password1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && password1.getText().length()<5 && password1.getText().length() >0) {
                    // code to execute when EditText loses focus
                    password1.setError("mot de passe trop court: minimum 5 caractères");
                }
                else if (password1.getText().length()>=5){
                    password1.setError(null);
                }
            }
        });



        /**
         * Controle sur l'age de l'utilisateur min 18 ans
         */
        dateNais.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s)
            {
                int age = getYears(dateNais.getText().toString());
                if (age < 18)
                {
                    dateNais.setError("Age min 18 ans");
                }

                else
                {
                    dateNais.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            { dateNais.setError(null);}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                dateNais.setError(null);
            }
        });

        datePickerDialog = new DatePickerDialog(getActivity(), pickerListener, year, month,day);
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        /**
         * inflater le menu; cela ajoute des itens a l'ActionBar si il est present
         */
        getActivity().getMenuInflater().inflate(R.menu.menu_creation_profil, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /**
         * gere le click sur la bare d'action.
         *
         */
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Déclancher lors de clique sur le bouton Créer
     * effectue le controle des champs avant la creation du profil utilisateur
     * @param view
     */
    public void modifierUtilisateur(View view) {

        boolean saisieValide = true;
        View focusView = null;


        strMdp = mdp.getText().toString();
        strConfMdp = confmdp.getText().toString();
       //strMail = mail.getText().toString();
        strDateNais = dateNais.getText().toString();


        strNom = nom.getText().toString();
        strPrenom = prenom.getText().toString();
        strAncienMdp = ancienMdp.getText().toString();
        if(radioM.isChecked()){
            strSex="M";
        }
        else{
            strSex="F";
        }

        if (TextUtils.isEmpty(strAncienMdp))
        {
            ancienMdp.setError("Champ Obligatoire");
            saisieValide = false;
            focusView = ancienMdp;
        }
      /*  if (TextUtils.isEmpty(strMdp))
        {
            mdp.setError("Champ Obligatoire");
            saisieValide = false;
            focusView = mdp;
        }
        if (TextUtils.isEmpty(strConfMdp))
        {
            confmdp.setError("Champ Obligatoire");
            saisieValide = false;
            focusView = confmdp;
        }

        if (TextUtils.isEmpty(strDateNais))
        {
            dateNais.setError("Champ obligatoire");
            saisieValide = false;
            focusView = dateNais;
        }*/



        System.out.println(" saisievalide = "+saisieValide);

        if(!saisieValide)
        {
            focusView.requestFocus();
        }
        else //(saisieValide)
        {
            // dateNais.setError(null);
            //  mdp.setError(null);
            //  confmdp.setError(null);

            ancienMdp.setError(null);
            if (strDateNais != null) {
                try {
                    String[] list = strDateNais.split("-");
                    int mm = Integer.valueOf(list[0].trim());
                    int dd = Integer.valueOf(list[1].trim());
                    int aaaa = Integer.valueOf(list[2].trim());

                    Log.d("annee", String.valueOf(aaaa));
                    Log.d("jour", String.valueOf(dd));
                    Log.d("mois", String.valueOf(mm));

                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    Date d = new Date(aaaa - 1900, mm - 1, dd);
                    strDateNais = sdf.format(d);

                    //Controle sur l'ancien mdp saisie
                    ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), strAncienMdp, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // The password is correct
                                ancienMdp.setError(null);

                                try {
                                    mettreAJourUtilisateur(strMdp, strPrenom, strNom, strDateNais, strSex);

                                } catch (java.text.ParseException e1) {
                                    e1.printStackTrace();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                            } else {
                                // The password was incorrect
                                ancienMdp.setError("Ancien mot de passe invalide");
                            }
                        }

                    });


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }


    /**
     * Ajout d'un nouvel utilisateur dans la base de donnees

     * @param password
     * @param firstName
     * @param lastName
     * @param birthDay
     * @param gender
     * @throws java.text.ParseException
     */
    private void mettreAJourUtilisateur(final String password,
                                        final String firstName,
                                        final String lastName,
                                        final String birthDay,
                                        final String gender) throws java.text.ParseException ,ParseException {

        System.out.println(" appel a mettreAJourUtilisateur");


        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        final Date dBirthDay = sdf.parse(birthDay);
        ParseUser pUser = ParseUser.getCurrentUser();
        if (password != null)
        {
            pUser.put("password", password);
         }
         pUser.put("firstname", firstName);
         pUser.put("lastname", lastName);
         pUser.put("gender", gender);

        if (dBirthDay != null) {
            pUser.put("birthday", dBirthDay);
        }

        pUser.saveInBackground();
        Toast.makeText(getActivity(), "Modification enregistrée",Toast.LENGTH_SHORT).show();

        Intent newActivity = new Intent(getActivity(), ProfilLoginActivity.class);
        startActivity(newActivity);
        getActivity().finish();
        System.out.println(" Fin appel a mettreAJourUtilisateur");

    }

             private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

                 @Override
                 public void onDateSet(DatePicker view, int selectedYear,
                                       int selectedMonth, int selectedDay) {

                     year = selectedYear;
                     month = selectedMonth;
                     day = selectedDay;


                     dateNais.setText(new StringBuilder().append(month + 1)
                             .append("-").append(day).append("-").append(year)
                             .append(" "));
                 }
             };


             /*** valdation du format de l'adresse mail
              *
              * @param emailAddress
              * @return
              */
             public boolean isValidEmailAddress(String emailAddress) {
                 String emailRegEx;
                 Pattern pattern;
                 // Regex for a valid email address
                 emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
                 // Compare the regex with the email address
                 pattern = Pattern.compile(emailRegEx);
                 Matcher matcher = pattern.matcher(emailAddress);
                 if (!matcher.find()) {
                     return false;
                 }
                 return true;
             }

             public View.OnTouchListener radioButtonListener = new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     // Is the button now checked?
                     boolean checked = ((RadioButton) v).isChecked();
                     // Check which radio button was clicked
                     switch (v.getId()) {
                         case R.id.radioM:
                             if (checked) {
                                 strSex = "M";
                             }
                             break;
                         case R.id.radioF:
                             if (checked) {
                                 strSex = "F";
                             }

                             break;
                     }
                     return false;
                 }
             };

             /**
              *  Calcul de l'age pour verifier si l'age est valide
              */

             public static int getYears(String strDate)

             {
                 Calendar curr = Calendar.getInstance();
                 Calendar birth = Calendar.getInstance();

                 SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                 try {
                     birth.setTime(sdf.parse(strDate));
                 } catch (java.text.ParseException e) {
                     e.printStackTrace();
                 }
                 int yeardiff = curr.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                 curr.add(Calendar.YEAR, -yeardiff);
                 if (birth.after(curr)) {
                     yeardiff = yeardiff - 1;
                 }
                 System.out.println("yeardiff==  " + yeardiff);
                 return yeardiff;
             }


             @Override
             public void onAttach(Activity activity) {
                 super.onAttach(activity);

                 // Activities containing this fragment must implement its callbacks.
                 if (!(activity instanceof CallbackFragment.Callbacks)) {
                     throw new IllegalStateException(
                             "Activity must implement fragment's callbacks.");
                 }

                 mCallbacks = (CallbackFragment.Callbacks) activity;
             }

             @Override
             public void onDetach() {
                 super.onDetach();

                 // Reset the active callbacks interface to the dummy implementation.
                 mCallbacks = sDummyCallbacks;
             }


         }


