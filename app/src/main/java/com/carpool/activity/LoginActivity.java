package com.carpool.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import com.carpool.model.Cote;
import com.carpool.model.Offre;
import com.carpool.model.Position;
import com.carpool.model.Reservation;
import com.carpool.model.Trajet;
import com.carpool.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.*;
import com.parse.ParseUser;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.carpool.model.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import static android.app.PendingIntent.getActivity;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends ActionBarActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };*/
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsername;
    private EditText mPasswordView;
    private View mProgressView;
    private View mEmailLoginFormView;
    //private SignInButton mPlusSignInButton;
    //private View mSignOutButtons;
    private View mLoginFormView;
    boolean succu;
    List<ParseObject> listUser;
    ParseUser utilis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
        ParseObject.registerSubclass(Cote.class);
        ParseObject.registerSubclass(Offre.class);
        ParseObject.registerSubclass(Position.class);
        ParseObject.registerSubclass(Reservation.class);
        ParseObject.registerSubclass(Trajet.class);
        ParseUser.registerSubclass(User.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "b0qpztTLfYQeRZBAeQGHX6pywO3pcCUorGfEbnAZ", "EDWDuQ4TYxmW7bZf7Yz51M2OJSXULsW9syUWaC58");*/







        /***
         *  Modifie par Jeanne
         *
         * on utilisera pas tout ce qui est service Google Play


        // Find the Google+ sign in button.
        mPlusSignInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
        if (supportsGooglePlayServices()) {
            // Set a listener to connect the user when the G+ button is clicked.
            mPlusSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        } else {
            // Don't offer G+ sign in if the app's version is too low to support Google Play
            // Services.
            mPlusSignInButton.setVisibility(View.GONE);
            return;
        }


        **/



        // Set up the login form.
        mUsername = (AutoCompleteTextView) findViewById(R.id.username);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        /*
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //2015-03-08
        //bouton ajouté pour la navigation vers la page de creation de profil
        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Modifie par Jeanne
                 */
                //on clique sur le bouton de creation du profil pour pouvoir creer le profil


                goToCreeProfil();
            }
        });



        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailLoginFormView = findViewById(R.id.email_login_form);



    }
/*
    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }*/


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // si l'authentification est reussie: Ici il faut retournee un booleen qui va specifier si l'authentification est "ok"
        /**
         *  modifie par Jeanne
         *  Ne pas s'etonner si l'authentification ne reussie pas
         */

        //if (mAuthTask != null) {
        //  return;
        //}

        // Reset errors.
        mUsername.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mUsername.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {

            if (!isPasswordValid(password)) {

                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
            /*
            else if (!isEmailValid(email)) {
                mUsername.setError(getString(R.string.error_invalid_email));
                focusView = mUsername;
                cancel = true;
            } else if (!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }*/

        }
        else if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;

            if (!isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                //focusView = null;
                focusView= mUsername;
                cancel = true;
            }

        }
        else if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {

            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;

        } else {
            // les deux sont vides

            mUsername.setError(getString(R.string.error_field_required));
            //focusView = null;
            focusView = mUsername;
            cancel = true;

            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;

        }




        /*
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError("mot de passe requis");
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)){
            mPasswordView.setError("Mot de passe invalide");
            focusView = mPasswordView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            // Ici l'authentification est a mettre

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

            //connexion a la base de donnee Parse
            // on recupere l'email et le mot de passe
            //
        }
    }

    private void goToCreeProfil(){

        Intent intent = new Intent(this, CreationProfilActivity.class);
        startActivity(intent);
    }

    /*
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.matches("^[a-z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,4}$");
        //return email.contains("@");
    }*/

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic

        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*@Override
    protected void onPlusClientSignIn() {
        //Set up sign out and disconnect buttons.
        /*Button signOutButton = (Button) findViewById(R.id.plus_sign_out_button);
        //signOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
       // Button disconnectButton = (Button) findViewById(R.id.plus_disconnect_button);
        //disconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });
    }*/
/*
    @Override
    protected void onPlusClientBlockingUI(boolean show) {
        showProgress(show);
    }*/
/*
    @Override
    protected void updateConnectButtonState() {
        //TODO: Update this logic to also handle the user logged in by email.
        boolean connected = getPlusClient().isConnected();

//        mSignOutButtons.setVisibility(connected ? View.VISIBLE : View.GONE);
        /***
         * modifie par jeanne
         */
        //mPlusSignInButton.setVisibility(connected ? View.GONE : View.VISIBLE);
        //mEmailLoginFormView.setVisibility(connected ? View.GONE : View.VISIBLE);
  //  }*/

    /*

    @Override
    protected void onPlusClientRevokeAccess() {
        // TODO: Access to the user's G+ account has been revoked.  Per the developer terms, delete
        // any stored user data here.
    }

    @Override
    protected void onPlusClientSignOut() {

    }*/

    /**
     * Check if the device supports Google Play Services.  It's best
     * practice to check first rather than handling this as an error case.
     *
     * @return whether the device supports Google Play Services

    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    } */

    /*@Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }*/

    /*@Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
*/
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }



        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // remplir le tableau dummy_credential avec les donnees de la base de donnees

            //connexion a la base de donnee Parse
            //final TextView texte = (TextView)findViewById(R.id.text_indic);

            /*
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
            query.whereEqualTo("email", "df11@yahoo.fr");
            //query.whereEqualTo("password", mPassword);

            query.getFirstInBackground(new GetCallback<ParseObject>()  {
                String text ;
                public void done(ParseObject object, ParseException e) {
                    //ParseObject user = new ParseObject("Users");
                        if (e == null) {

                            // object will be your game score
                            texte.setText("objet retrouvee "+object.getString("username"));
                            succu = true;

                        } else {
                            // something went wrong
                            succu = false;
                            texte.setText("erreur "+object.getString("username"));
                        }
                    }
            });*/

            /*
            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("email", mEmail);
            query.whereEqualTo("password", "");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {
                        if (scoreList.size() != 0) {
                            listUser = scoreList;

                            Log.d("email", "Retrieved " + scoreList.get(0).getObjectId() + " scores");
                            texte.setText(scoreList.get(0).getString("password"));
                            //succu = true;
                        }


                    } else {
                        Log.d("username", "Error: " + e.getMessage());
                        texte.setText(scoreList.get(0).getString("username"));
                        //succu = false;
                    }
                }
            });*/

            ParseUser.logInInBackground(mEmail, mPassword.trim().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null)
                    {
                        utilis = user;
                        //Log.d("if", utilis.getObjectId());


                    }else
                    {
                        //Log.d("failed", "failed");
                    }
                }
            });


           // User user;
           // string userName = user.setUsername().toString();

/*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return false;*/
            //texte.setText(String.valueOf(succu));

            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(utilis != null) {
                //if (listUser.size() == 1) {

                    Log.d("bon ", utilis.getObjectId());
                    return true;
                //}
               // else {

                    //Log.d("requete faite ", "aucun utilisateur trouvee");
                   // return false;
               // }
            }
            //Log.d("erreur", "erreur null");
            return false;

        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                Intent newActivity = new Intent(LoginActivity.this, AccueilActivity.class);
                startActivity(newActivity);
                finish();
            } else {

                TextView texte = (TextView)findViewById(R.id.text_indic);
                texte.setText("Pseudo ou mot de passe incorrect");

                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



