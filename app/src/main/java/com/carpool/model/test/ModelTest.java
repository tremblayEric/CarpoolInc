package com.carpool.model.test;

import android.test.ActivityInstrumentationTestCase2;

import com.carpool.model.data.CoteDTO;
import com.carpool.model.data.OffreDTO;
import com.carpool.model.data.PositionDTO;
import com.carpool.model.data.ReservationDTO;
import com.carpool.model.data.TrajetDTO;
import com.carpool.model.data.UserDTO;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SignUpCallback;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import mgl7130.tiroir.LoginActivity;

/**
 * Created by Gaëlle on 3/6/2015.
 */

public class ModelTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    public ModelTest() {
        super(LoginActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        getActivity();
    }

    // Test parse Offre
    public void testOffre() {
        OffreDTO oo = new OffreDTO();
        oo.setNbreProposition(3);
        oo.setReservationCount(3);
        oo.saveInBackground();
    }


    // Test parse Cote
    public void testCote() {
            CoteDTO cc = new CoteDTO();
            cc.setCommentaire("Bad ride");
            cc.setNote(4);
            cc.saveInBackground();
    }


    // Test parse Position
    public void testPosition() {
        PositionDTO pp = new PositionDTO();
        pp.setLatitude(50);
        pp.setLongitude(50);
        pp.saveInBackground();
    }

    // Test parse Reservation
    public void testReservation() {
        ReservationDTO rr = new ReservationDTO();
        rr.setStatut(ReservationDTO.ReservationStatut.ATTENTE);
    }

    // Test parse Trajet
    public void testTrajet() {
        TrajetDTO tt = new TrajetDTO();
        tt.setDepart(new Date());
        tt.setHeureDebut(new Date());
        tt.setHeureFin(new Date());
        PositionDTO pos = new PositionDTO();
        pos.setLongitude(20);
        pos.setLatitude(20);
        tt.setPositionDepart(pos);
        PositionDTO pos2 = new PositionDTO();
        pos2.setLongitude(30);
        pos2.setLatitude(30);
        tt.setPositionArrive(pos2);
        tt.saveInBackground();
    }

    // Test parse user
    public void testUser() {
        UserDTO us = new UserDTO();
        us.setUsername(getUsername());
        us.setPassword("1234");

        us.setEmail(getUsername()+"@sympatico.ca");
        us.setFirstname("Joëlle");
        us.setLastname("Alifax");
        us.setBirthday(new Date());
        us.setGender(UserDTO.UserGender.F);
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
        ParseQuery<CoteDTO> query = ParseQuery.getQuery(CoteDTO.class);
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
