package com.carpool.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.carpool.CarpoolApplication;
import com.carpool.model.Cote;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Reservation;
import com.carpool.model.Trajet;
import com.carpool.model.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

// Class Création d'un profil utilisateur
public class CreationProfilActivity extends Activity {

    // Variables representant les composants de l'ui
    private EditText pseudo;
    private  EditText mdp;
    private  EditText confmdp ;
    private  EditText mail ;
    private  EditText dateNais ;
    private  EditText nom ;
    private  EditText prenom;
    private  TextView compteCree;
    private RadioButton radioF;

    static String strPseudo ;
    static String strMdp ;
    static String strConfMdp ;
    static String strMail ;
    static String strDateNais;
    static String strSex ;
    static String strNom;
    static String strPrenom ;
    static String sexeF;


    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;


    public static boolean validationPseudoDone = false;
    static boolean pseudoExisteInDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creation_profil);


        pseudo = (EditText) findViewById(R.id.txtPseudo);
        mdp = (EditText) findViewById(R.id.txtMdp);
        confmdp = (EditText) findViewById(R.id.txtConfMdp);
        mail = (EditText) findViewById(R.id.txtCourriel);
        dateNais = (EditText) findViewById(R.id.txtCalendar);
        nom = (EditText) findViewById(R.id.txtNom);
        prenom = (EditText) findViewById(R.id.txtPrenom);
        compteCree = (TextView) findViewById(R.id.compteCree);
        radioF = (RadioButton)findViewById(R.id.radioF);
        strSex = radioF.getText().toString();


        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);


        // gestion affichage calendrier
        final EditText txtCalendar = (EditText) findViewById(R.id.txtCalendar);
        txtCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(DATE_PICKER_ID);
                return false;
            }
        });


        final Button buttonCreation = (Button) findViewById(R.id.buttonCreer);
        buttonCreation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                ajouterUtilisateur(v);
            }
        });

        // vérification de la confirmation de mot de passe
        final EditText password1 = (EditText) findViewById(R.id.txtMdp);
        final EditText password2 = (EditText) findViewById(R.id.txtConfMdp);
        final TextView error = (TextView) findViewById(R.id.TextView_PwdProblem);
        strMdp = password1.getText().toString();

     //   if (!TextUtils.isEmpty(strMdp) && !isPasswordValid(strMdp)) {
      //      password1.setError("Mot de passe très court");
       // }



        password2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = password1.getText().toString();
                String strPass2 = password2.getText().toString();
                if (!strPass1.equals(strPass2)) {
                   // error.setText(R.string.settings_pwd_not_equal);
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
                    //error.setText(R.string.settings_pwd_not_equal);
                    password2.setError("Confirmation invalide");

                }
                else{
                    password2.setError(null);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        // vérification du mail
        final EditText txtCourriel = (EditText) findViewById(R.id.txtCourriel);
        final TextView errormail = (TextView) findViewById(R.id.TextView_mailProblem);

        txtCourriel.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && txtCourriel.getText().length()>0) {
                    // code to execute when EditText loses focus
                    if(!isValidEmailAddress(txtCourriel.getText().toString()))
                    {
                       // errormail.setText("Email invalide");
                        txtCourriel.setError("Email invalide");
                    }
                    else{
                        txtCourriel.setError(null);
                    }

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creation_profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /** Called when the user clicks the Send button */
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
            dateNais.setError("date de naissance requise");
        }
        else {
            int age = getYears(strDateNais);
            if (age < 18) {
                dateNais.setError("Age min 18 ans");
                saisieValide = false;
                focusView = dateNais;
            }
        }
       // validerPseudoInDB();
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


    private void creerUtilisateur(String userName, String password, String email,
                                  String firstName, String lastName,
                                  String birthDay,String gender) throws ParseException {
        View focusView = null;
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

                    Intent newActivity = new Intent(CreationProfilActivity.this, AccueilActivity.class);
                    startActivity(newActivity);
                    finish();

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

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener()
    {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // SOutputhow selected date
            dateNais.setText(new StringBuilder().append(month+1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };


    // valdation adresse mail
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioM:
                if (checked) strSex="M";
                break;
            case R.id.radioF:
                if (checked) strSex="F";
                break;
        }
    }
// Calcul de l'age

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
