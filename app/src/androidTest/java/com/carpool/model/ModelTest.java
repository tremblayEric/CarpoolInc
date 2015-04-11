package com.carpool.model;

import android.test.ActivityInstrumentationTestCase2;
import com.carpool.ui.activities.AccueilActivity;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SignUpCallback;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/***
 * Classe de test pour les modèles de données se trouvant dans le package model.
 */
/**
 * Created by Gaëlle on 3/6/2015.
 */
public class ModelTest extends ActivityInstrumentationTestCase2<AccueilActivity> {

    public ModelTest() {
        super(AccueilActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        getActivity();
    }

    // Test parse Cote
    public void testCote() {
        Cote cc = new Cote();
        cc.setCommentaire("Bad ride");
        cc.setNote(4);
        cc.saveInBackground();
    }

    // Test parse Position
    public void testPosition() {
        Position pp = new Position();
        pp.setLatitude(50);
        pp.setLongitude(50);
        pp.saveInBackground();
    }

    // Test parse Reservation
    public void testReservation() {
        Reservation rr = new Reservation();
        //rr.setStatut(Reservation.ReservationStatut.ATTENTE);
    }

    // Test parse Trajet
    public void testTrajetAvecParentOffre() {
        Trajet tt = new Trajet();
        Position pos = new Position();
        pos.setLongitude(20);
        pos.setLatitude(20);
        tt.setPositionDepart(pos);
        Position pos2 = new Position();
        pos2.setLongitude(30);
        pos2.setLatitude(30);
        tt.setPositionArrive(pos2);


        Offre oo = new Offre();
        oo.setDepart(new Date());
        oo.setHeureDebut(new Date());
        oo.setHeureFin(new Date());
        oo.setNbreProposition(3);
        oo.setReservationCount(3);

        oo.setTrajet(tt);

        oo.saveInBackground();
    }

    // Test parse user
    public void testUser() {
        User us = new User();
        us.setUsername(getUsername());
        us.setPassword("1234");

        us.setEmail(getUsername()+"@sympatico.ca");
        us.setFirstname("Joëlle");
        us.setLastname("Alifax");
        us.setBirthday(new Date());
        us.setGender(User.UserGender.F);
        us.saveInBackground();

        us.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    // Test parse query
    public void testQuery(){
        ParseQuery<Cote> query = ParseQuery.getQuery(Cote.class);
        query.whereEqualTo("note", 4).countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                Logger.getLogger("Counter").log(Level.INFO, "the count result is : " + i);
            }
        });
    }

    public String getUsername(){
        Random rand = new Random();
        String user = System.getProperty("user.name", String.valueOf(rand.nextInt(5500)));

        return user + String.valueOf(rand.nextInt(5500));
    }

}
