package com.carpool;

import android.app.Application;

import com.carpool.model.Offre;
import com.carpool.model.Cote;
import com.carpool.model.Reservation;
import com.carpool.model.Position;
import com.carpool.model.Trajet;
import com.carpool.model.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    }


}
