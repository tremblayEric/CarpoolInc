package com.carpool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.PlusClient;

/**
 * A base class to wrap communication with the Google Play Services PlusClient.
 */
public abstract class PlusBaseActivity extends ActionBarActivity {

    // A magic number we will use to know that our sign-in error resolution activity has completed
    private static final int OUR_REQUEST_CODE = 49404;

    // A flag to stop multiple dialogues appearing for the user
    private boolean mAutoResolveOnFail;

    // A flag to track when a connection is already in progress
    public boolean mPlusClientIsConnecting = false;

    // This is the helper object that connects to Google Play Services.
    // The saved result from {@link #onConnectionFailed(ConnectionResult)}.  If a connection
    // attempt has been made, this is non-null.
    // If this IS null, then the connect method is still running.
    private ConnectionResult mConnectionResult;


    /**
     * Called when the {@link PlusClient} revokes access to this app.
     */
   // protected abstract void onPlusClientRevokeAccess();

    /**
     * Called when the PlusClient is successfully connected.
     */
    //protected abstract void onPlusClientSignIn();

    /**
     * Called when the {@link PlusClient} is disconnected.
     */
    //protected abstract void onPlusClientSignOut();

    /**
     * Called when the {@link PlusClient} is blocking the UI.  If you have a progress bar widget,
     * this tells you when to show or hide it.
     */
    protected abstract void onPlusClientBlockingUI(boolean show);

    /**
     * Called when there is a change in connection state.  If you have "Sign in"/ "Connect",
     * "Sign out"/ "Disconnect", or "Revoke access" buttons, this lets you know when their states
     * need to be updated.
     */
    protected abstract void updateConnectButtonState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the PlusClient connection.
        // Scopes indicate the information about the user your application will be able to access.
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setProgressBarVisible(boolean flag) {
        mPlusClientIsConnecting = flag;
        onPlusClientBlockingUI(flag);
    }

    /**
     * An earlier connection failed, and we're now receiving the result of the resolution attempt
     * by PlusClient.
     *
     * see #onConnectionFailed(ConnectionResult)
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        updateConnectButtonState();
        if (requestCode == OUR_REQUEST_CODE && responseCode == RESULT_OK) {
            // If we have a successful result, we will want to be able to resolve any further
            // errors, so turn on resolution with our flag.
            mAutoResolveOnFail = true;
            // If we have a successful result, let's call connect() again. If there are any more
            // errors to resolve we'll get our onConnectionFailed, but if not,
            // we'll get onConnected.
        } else if (requestCode == OUR_REQUEST_CODE && responseCode != RESULT_OK) {
            // If we've got an error we can't resolve, we're no longer in the midst of signing
            // in, so we can stop the progress spinner.
            setProgressBarVisible(false);
        }
    }
}