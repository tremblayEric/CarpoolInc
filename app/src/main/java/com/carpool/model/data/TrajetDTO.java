package com.carpool.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Trajet")
public class TrajetDTO extends ParseObject implements Serializable {
    PositionDTO positionDepart;
    PositionDTO positionArrive;
    Date depart;
    Timestamp heureDebut;
    Timestamp heureFin;

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

    public void setPositionDepart(PositionDTO positionDepart)
    {
        put("positionDepart", positionDepart);
    }

    public PositionDTO getPositionDepart()
    {
        return (PositionDTO)getParseObject("positionDepart");
    }

    public void setPositionArrive(PositionDTO positionArrive)
    {
        put("positionArrive", positionArrive);
    }

    public PositionDTO getPositionArrive()
    {
        return (PositionDTO)getParseObject("positionArrive");
    }
}

