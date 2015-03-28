package com.carpool.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.carpool.model.User;
import com.parse.SignUpCallback;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;


// Class Création d'un profil utilisateur
public class CreationProfilActivity extends Fragment {

    View rootview;

    DatePickerDialog datePickerDialog;

    // Variables representant les composants de l'ui
    private EditText pseudo;
    private  EditText mdp;
    private  EditText confmdp ;
    private  EditText mail ;
    private  EditText dateNais ;
    private  EditText nom ;
    private  EditText prenom;
    private  TextView compteCree;

    static String strPseudo ;
    static String strMdp ;
    static String strConfMdp ;
    static String strMail ;
    static String strDateNais;
    static String strSex ;
    static String strNom;
    static String strPrenom ;

    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_creation_profil, container, false);

        /**
         * Recuperer les valeurs des composants dans des variables
         */
        pseudo = (EditText) rootview.findViewById(R.id.txtPseudo);
        mdp = (EditText) rootview.findViewById(R.id.txtMdp);
        confmdp = (EditText) rootview.findViewById(R.id.txtConfMdp);
        mail = (EditText) rootview.findViewById(R.id.txtCourriel);
        dateNais = (EditText) rootview.findViewById(R.id.txtCalendar);
        nom = (EditText) rootview.findViewById(R.id.txtNom);
        prenom = (EditText) rootview.findViewById(R.id.txtPrenom);
        compteCree = (TextView) rootview.findViewById(R.id.compteCree);
        strSex="F";
        ((RadioButton)rootview.findViewById(R.id.radioF)).setOnTouchListener(radioButtonListener);
        ((RadioButton)rootview.findViewById(R.id.radioM)).setOnTouchListener(radioButtonListener);
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
        final EditText txtCalendar = (EditText) rootview.findViewById(R.id.txtCalendar);
        txtCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                datePickerDialog.show();
                return false;
            }
        });

        /**
         *  mettre une icone font-awesome sur le boutton de creation du profil
         */
        final Button buttonCreation = (Button) rootview.findViewById(R.id.buttonCreer);

        Typeface font = Typeface.createFromAsset( getActivity().getAssets(), "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );
        buttonCreation.append("    CREATION");

        buttonCreation.setTypeface(font);

        /**
         *  effectue l'ajout des utilisateur au click sur un bouton
         */
        buttonCreation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                ajouterUtilisateur(v);
            }
        });


        /**
         *  Controle sur la confirmation de Mdp
         */
        final EditText password1 = (EditText) rootview.findViewById(R.id.txtMdp);
        final EditText password2 = (EditText) rootview.findViewById(R.id.txtConfMdp);

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
         * Controle sur le format de l'adresse email
         */
        final EditText txtCourriel = (EditText) rootview.findViewById(R.id.txtCourriel);
        txtCourriel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && txtCourriel.getText().length()>0) {
                    // code to execute when EditText loses focus
                    if(!isValidEmailAddress(txtCourriel.getText().toString()))
                    {
                        txtCourriel.setError("Email invalide");
                    }
                    else
                    {
                        txtCourriel.setError(null);
                    }
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
    public void ajouterUtilisateur(View view) {

        boolean saisieValide = true;
        View focusView = null;

        strPseudo = pseudo.getText().toString();
        strMdp = mdp.getText().toString();
        strConfMdp = confmdp.getText().toString();
        strMail = mail.getText().toString();
        strDateNais = dateNais.getText().toString();
        strNom = nom.getText().toString();
        strPrenom = prenom.getText().toString();

       if (TextUtils.isEmpty(strPseudo))
       {
           pseudo.setError("Champ Obligatoire");
           saisieValide = false;
           focusView = pseudo;
      }
      if (TextUtils.isEmpty(strMdp))
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
        if (TextUtils.isEmpty(strMail))
       {
          mail.setError("Champ Obligatoire");
          saisieValide = false;
           focusView = mail;
       }

        if (TextUtils.isEmpty(strDateNais))
        {
            dateNais.setError("Champ obligatoire");
            saisieValide = false;
            focusView = dateNais;
        }

        if (TextUtils.isEmpty(strMail))
        {
            mail.setError("Champ obligatoire");
            saisieValide = false;
            focusView = mail;
        }

        System.out.println(" saisievalide = "+saisieValide);

        if(!saisieValide)
        {
            focusView.requestFocus();
        }
         else //(saisieValide)
          {
              dateNais.setError(null);
              pseudo.setError(null);
              mdp.setError(null);
              confmdp.setError(null);
              mail.setError(null);
            try {
                String[] list = strDateNais.split("-");
                int mm = Integer.valueOf(list[0].trim());
                int dd = Integer.valueOf(list[1].trim());
                int aaaa = Integer.valueOf(list[2].trim());

                Log.d("annee", String.valueOf(aaaa));
                Log.d("jour", String.valueOf(dd));
                Log.d("mois", String.valueOf(mm));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date(aaaa-1900,mm-1,dd);
                strDateNais = sdf.format(d);
                creerUtilisateur(strPseudo, strMdp,strMail, strPrenom, strNom,strDateNais,strSex);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }


    /**
     * Ajout d'un nouvel utilisateur dans la base de donnees
     * @param userName
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param birthDay
     * @param gender
     * @throws ParseException
     */
    private void creerUtilisateur(String userName, String password, String email,
                                  String firstName, String lastName,
                                  String birthDay,String gender) throws ParseException {

        System.out.println(" appel a creer Utilisateur");
        User us = new User();
        us.setUsername(userName);
        us.setPassword(password);
        us.setEmail(email);
        us.setFirstname(firstName);
        us.setLastname(lastName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        us.setBirthday(sdf.parse(birthDay));
        us.setGender(User.UserGender.valueOf(gender));
        us.saveInBackground();
        System.out.println("Fin saveinBackground");

        us.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    System.out.println("Exception signUpInBackground is null  ");
                    compteCree.setError(null);
                    compteCree.setText("Compte Créé avec succès!");
                    compteCree.setTextColor(Color.parseColor("#ff80cbc4"));

                    Intent newActivity = new Intent(getActivity(), AccueilActivity.class);
                    startActivity(newActivity);
                    getActivity().finish();

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    System.out.println("Exception signUpInBackground is not null  "+e);
                    compteCree.setError(e.getMessage().toString());
                    compteCree.setText("Pseudo ou email déjà utilisé");

                }
            }
        });

        System.out.println(" Fin appel a creer Utilisateur");
  }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener()
    {

        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;


            dateNais.setText(new StringBuilder().append(month+1)
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
            switch(v.getId()) {
                case R.id.radioM:
                    if (checked)  {strSex="M"; }
                    break;
                case R.id.radioF:
                    if (checked) {strSex="F"; }

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
        Calendar birth =  Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        try {
            birth.setTime(sdf.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int yeardiff = curr.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        curr.add(Calendar.YEAR,-yeardiff);
        if(birth.after(curr))
        {
            yeardiff = yeardiff - 1;
        }
        System.out.println("yeardiff==  "+yeardiff);
        return yeardiff;
    }

}
