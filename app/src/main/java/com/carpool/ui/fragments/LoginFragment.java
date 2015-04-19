package com.carpool.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.carpool.R;
import com.carpool.ui.activities.CreationProfilActivity;
import com.carpool.ui.activities.RechercheActivity;
import com.carpool.ui.design.CallbackFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

public class LoginFragment extends CallbackFragment {

    View rootview;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_login,container,false);

        texte = (TextView)rootview.findViewById(R.id.error_connection);

        mUsername = (AutoCompleteTextView) rootview.findViewById(R.id.username);

        mPasswordView = (EditText) rootview.findViewById(R.id.password);
        Button mEmailSignInButton = (Button) rootview.findViewById(R.id.email_sign_in_button);



        /**
         * mise en forme du bouton de connexion avec des icones fontAwesome
         */
        Typeface font = Typeface.createFromAsset( getActivity().getAssets(),
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
        //TextView icone_pseudo = (TextView)rootview.findViewById(R.id.icone_pseudo);
       // icone_pseudo.setTypeface(font);
        mUsername.setTypeface(font);
        mPasswordView.setTypeface(font);

        //TextView icone_psswd = (TextView)rootview.findViewById(R.id.icone_psswd);
       // icone_psswd.setTypeface(font);

        Button registerButton = (Button) rootview.findViewById(R.id.register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                goToCreeProfil();
            }
        });
        registerButton.append("    INSCRIPTION");
        registerButton.setTypeface(font);

        mLoginFormView = rootview.findViewById(R.id.login_form);
        mProgressView = rootview.findViewById(R.id.login_progress);
        mEmailLoginFormView = rootview.findViewById(R.id.email_login_form);

        fin = true;

        return rootview;
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


        Intent intent = new Intent(getActivity(), CreationProfilActivity.class);
        startActivity(intent);
        getActivity().finish();
        /*
        Fragment objFragment = new CreationProfilFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();*/
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
                    if (user != null) {
                        utilis = user;

                    }
                    flag = true;
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

                // Subscribe to channel "Offre"
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("user", ParseUser.getCurrentUser());
                installation.saveInBackground();
                ParsePush.subscribeInBackground("Offre");

                Intent newActivity  = new Intent(getActivity(), RechercheActivity.class);
                startActivity(newActivity);
                getActivity().finish();

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
}