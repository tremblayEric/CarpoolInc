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

/**
 * Created by Gaëlle on 3/6/2015.
 */
public class CarpoolApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

   System.out.println("CarpoolApplication.oncreate");
        ParseObject.registerSubclass(Cote.class);
        ParseObject.registerSubclass(Offre.class);
        ParseObject.registerSubclass(Position.class);
        ParseObject.registerSubclass(Reservation.class);
        ParseObject.registerSubclass(Trajet.class);
        ParseUser.registerSubclass(User.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "b0qpztTLfYQeRZBAeQGHX6pywO3pcCUorGfEbnAZ", "EDWDuQ4TYxmW7bZf7Yz51M2OJSXULsW9syUWaC58");
        System.out.println("CarpoolApplication.oncreate fin connx");
    }


}
