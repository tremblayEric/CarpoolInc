package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

/***
 * Classe représentant le concept de réservation (une réservation est faite par un utilisateur
 * sur une offre de covoiturage annoncée par un autre utilisateur. La réservation doit être
 * acceptée par l'annonceur.
 */
@ParseClassName("Reservation")
public class Reservation extends ParseObject implements Serializable {

    Offre offreSource;
    User userDemandeur;

    public Reservation(){

    }


    public ParseUser getUserDemandeur() {

           return (ParseUser)getParseUser("userDemandeur");
    }

    public void setUserDemandeur(ParseUser userDemandeur) {
           put("userDemandeur", userDemandeur);
    }

    public Offre getOffreSource() {

        return (Offre)getParseObject("offreSource");
    }

    public void setOffreSource(Offre offreSource) {
        put("offreSource", offreSource);
    }


    public enum ReservationStatut {
        OK, NON, ATTENTE;
    };

    public ReservationStatut getReservationStatut() {
        return ReservationStatut.valueOf(getString("reservationStatut"));
    }

    public void setReservationStatut(ReservationStatut statut) {
        put("reservationStatut", statut.toString());
    }
}
