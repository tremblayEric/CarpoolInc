package com.carpool.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import com.parse.*;
import com.parse.ParseUser;
import android.graphics.Typeface;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {


    private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mUsername;
    private EditText mPasswordView;
    private View mProgressView;
    private View mEmailLoginFormView;
    private View mLoginFormView;
    ParseUser utilis;
    boolean flag = false;
    private Toolbar toolbar;
    boolean fin = false;
    TextView texte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        texte = (TextView)findViewById(R.id.error_connection);

        /***
         * gestion de la barre la toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setLogo(R.drawable.carpool_logo);
            toolbar.setTitle("      CARPOOL INC.       ");
            toolbar.setTitleTextColor(Color.WHITE);
            Log.d("toolbar", "dans la toolbar");
        }


        mUsername = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        /**
         * mise en forme du bouton de connexion avec des icones fontAwesome
         */
        Typeface font = Typeface.createFromAsset( getAssets(),
                "font-awesome-4.3.0/fonts/fontawesome-webfont.ttf" );
        mEmailSignInButton.append("    CONNEXION");
        mEmailSignInButton.setTypeface(font);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        /**
         *
         * on clique sur le bouton de creation du profil pour pouvoir creer le profil
         */

        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                goToCreeProfil();
            }
        });
        registerButton.append("    INSCRIPTION");
        registerButton.setTypeface(font);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailLoginFormView = findViewById(R.id.email_login_form);

        fin = true;
    }


    /**
     * fonction qui permet de se connecter
     */
    public void attemptLogin() {

        /**
         * reinitialiser les erreus
         */
        mUsername.setError(null);
        mPasswordView.setError(null);

        /**
         * sauvegarder les valeurs au moment de la tentative de connexion
         */
        String email = mUsername.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /**
         * verifier si mot de passe valide
         */

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {

            if (!isPasswordValid(password)) {

                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
        }
        else if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;

            if (!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView= mUsername;
                cancel = true;
            }

        }
        else if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {

            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;

        } else {
            mUsername.setError(getString(R.string.error_field_required));
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            /**
             * donner le focus au premier controle sur lequel on trouve une erreur
             */
            focusView.requestFocus();
        } else {

            /**
             * montrer le progress spinner et executer la tache de tentative de login en background
             */
            flag = false;
            fin = false;
            showProgress(true);

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
        }
    }

    private void goToCreeProfil(){

        Intent intent = new Intent(this, CreationProfilActivity.class);
        startActivity(intent);
    }

    /**
     * verifier la longueur du mot de passe
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic

        return password.length() > 4;
    }

    /**
     * montre le progress UI du spinner et cache la tache de login en background
     * @param show
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * connexion asynchrone et tache utilisee pour authentifier l'utilisateur en arriere plan
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            ParseUser.logInInBackground(mEmail, mPassword.trim().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null)
                    {
                        utilis = user;

                    }flag = true;
                }
            });

            /**
             * Ici on attend que la variable utilis soit mis a jour pour verifier si la connexion
             * a reussie ou pas. Bien plus efficace que le Thread.sleep car tient compte de la lenteur propre
             * a chaque reseau
             */
            while (!flag)
            {

            }

            if(utilis != null) {
                    return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            showProgress(true);

            if (success) {

                texte.setText("");
                Log.d("utilis", utilis.getObjectId());
                Log.d("trouve", "utilisateur touve");
                Intent newActivity  = new Intent(LoginActivity.this, AccueilActivity.class);
                startActivity(newActivity);

                finish();

            } else {
                      showProgress(false);

                      texte.setText("** Pseudo ou mot de passe incorrect **");
                      texte.setTextColor(Color.RED);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}