package com.carpool.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Reservation")
public class ReservationDTO extends ParseObject implements Serializable {

    public ReservationDTO(){

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
