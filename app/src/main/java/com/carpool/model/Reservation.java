package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.io.Serializable;

@ParseClassName("Reservation")
public class Reservation extends ParseObject implements Serializable {

    public Reservation(){

    }

    public enum ReservationStatut {
        OK, NON, ATTENTE;
    };

    public ReservationStatut getStatut() {
        return ReservationStatut.valueOf(getString("statut"));
    }

    public void setStatut(ReservationStatut statut) {
        put("statut", statut.toString());
    }
}
