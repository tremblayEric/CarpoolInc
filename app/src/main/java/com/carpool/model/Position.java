package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Position")
public class Position extends ParseObject implements Serializable {

    public Position(){

    }

    public double getLatitude() {
        return getInt("latitude");
    }

    public void setLatitude(double latitude) {
        put("latitude", latitude);
    }

    public double getLongitude() {
        return getInt("longitude");
    }

    public void setLongitude(double longitude) {
        put("longitude", longitude);
    }
}
