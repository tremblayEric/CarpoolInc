package com.carpool.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Offre")
public class OffreDTO extends ParseObject implements Serializable {

    public OffreDTO(){

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

    public void setReservationCount(int reservationCount) {
        put("reservationCount", reservationCount);
    }
}
