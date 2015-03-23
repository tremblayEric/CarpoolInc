package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/***
 * Classe représentant le concept de cote (une cote est une note attribuée par un utilisateur à
 * un autre utilisateur.
 */
@ParseClassName("Cote")
public class Cote extends ParseObject implements Serializable {

    public Cote(){

    }

    public int getNote() {
        return getInt("note");
    }

    public void setNote(int note) {
        put("note", note);
    }

    public String getCommentaire() {
        return getString("commentaire");
    }

    public void setCommentaire(String commentaire) {
        put("commentaire", commentaire);
    }

}
