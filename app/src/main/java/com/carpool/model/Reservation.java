package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.io.Serializable;

/***
 * Classe représentant le concept de réservation (une réservation est faite par un utilisateur
 * sur une offre de covoiturage annoncée par un autre utilisateur. La réservation doit être
 * acceptée par l'annonceur.
 */
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
