package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

/***
 * Classe représentant le concept d'Offre (une offre est une annonce de covoiturage faite par un
 * utilisateur et est reliée à un trajet.
 */
@ParseClassName("Offre")
public class Offre extends ParseObject implements Serializable {

    Trajet trajet ;

    User user;

    public Offre(){

    }

    public Trajet getTrajet() {
        return (Trajet)getParseObject("trajetOffre");
    }

    public void setTrajet(Trajet trajet) {
        put("trajetOffre", trajet);
    }

    public ParseUser getUser() {
        return (ParseUser)getParseUser("userOffre");
    }

    public void setUser(ParseUser user) {
        put("userOffre", user);
    }

    public int getNbreProposition() {
        return getInt("nbreProposition");
    }

    public void setNbreProposition(int nbreProposition) {
        put("nbreProposition", nbreProposition);
    }

    public int getReservationCount() {
        return getInt("reservationCount");
    }

    public void setReservationCount(int reservationCount)
    {
        put("reservationCount", reservationCount);
    }

    public Date getDepart() {
        return getDate("depart");
    }

    public void setDepart(Date depart) {
        put("depart", depart);
    }

    public Date getHeureDebut() {
        return getDate("heureDebut");
    }

    public void setHeureDebut(Date heureDebut) {
        put("heureDebut", heureDebut);
    }

    public Date getHeureFin() {
        return getDate("heureFin");
    }

    public void setHeureFin(Date heureFin) {
        put("heureFin", heureFin);
    }

}
