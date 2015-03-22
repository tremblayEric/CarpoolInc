package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;


@ParseClassName("Position")
public class Position extends ParseObject implements Serializable {

    public Position(){

    }

    public double getLatitude() {
        return getDouble("latitude");
    }

    public void setLatitude(double latitude) {
        put("latitude", latitude);
    }

    public double getLongitude() {
        return getDouble("longitude");
    }

    public void setLongitude(double longitude) {
        put("longitude", longitude);
    }
}
