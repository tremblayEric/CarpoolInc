package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Trajet")
public class Trajet extends ParseObject implements Serializable {
    Position positionDepart;
    Position positionArrive;



    public void setPositionDepart(Position positionDepart)
    {
        put("positionDepart", positionDepart);
    }

    public Position getPositionDepart()
    {
        return (Position)getParseObject("positionDepart");
    }

    public void setPositionArrive(Position positionArrive)
    {
        put("positionArrive", positionArrive);
    }

    public Position getPositionArrive()
    {
        return (Position)getParseObject("positionArrive");
    }
}

