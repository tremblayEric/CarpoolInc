package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.io.Serializable;

@ParseClassName("Trajet")
public class Trajet extends ParseObject implements Serializable {

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

