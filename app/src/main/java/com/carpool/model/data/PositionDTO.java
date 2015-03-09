package com.carpool.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Position")
public class PositionDTO extends ParseObject implements Serializable {

    public PositionDTO(){

    }

    public int getLatitude() {
        return getInt("latitude");
    }

    public void setLatitude(int latitude) {
        put("latitude", latitude);
    }

    public int getLongitude() {
        return getInt("longitude");
    }

    public void setLongitude(int longitude) {
        put("longitude", longitude);
    }
}
