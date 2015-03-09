package com.carpool.model.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Gaëlle on 3/5/2015.
 */
@ParseClassName("Cote")
public class CoteDTO extends ParseObject implements Serializable {

    public CoteDTO(){

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
