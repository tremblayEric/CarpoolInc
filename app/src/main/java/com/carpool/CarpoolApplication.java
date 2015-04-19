package com.carpool;

import android.app.Application;
import android.util.Log;

import com.carpool.model.Offre;
import com.carpool.model.Cote;
import com.carpool.model.Reservation;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.carpool.model.User;
import com.carpool.ui.activities.RechercheActivity;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/***
 * Classe qui s'occupe d'initialiser certains paramètres uniquement au lancement de l'application.
 * C'est ici que Parse s'initialise.
 */
public class CarpoolApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Cote.class);
        ParseObject.registerSubclass(Offre.class);
        ParseObject.registerSubclass(Position.class);
        ParseObject.registerSubclass(Reservation.class);
        ParseObject.registerSubclass(Trajet.class);
        ParseUser.registerSubclass(User.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "b0qpztTLfYQeRZBAeQGHX6pywO3pcCUorGfEbnAZ", "EDWDuQ4TYxmW7bZf7Yz51M2OJSXULsW9syUWaC58");

        // Save the current Installation to Parse.
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        if(ParseUser.getCurrentUser() != null) {
            // Activer les push notification - S'abonner au canal de distribution
            installation.put("user",ParseUser.getCurrentUser());
            installation.saveInBackground();
            ParsePush.subscribeInBackground("Offre");
        }
        else{
            installation.saveInBackground();
            ParsePush.unsubscribeInBackground("Offre");
        }
    }


}
