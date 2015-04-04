package com.carpool.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;

/***
 * Classe représentant le concept d'utilisateur (un utilisateur est une personne qui s'est créée
 * un compte en fournissant des informations personnelles.
 */
@ParseClassName("User")
public class User extends ParseUser implements Serializable {
    public User() {

    }
    public enum UserGender {
        F, M
    }

    public String getFirstname(){
        return getString("firstname");
    }

    public void setFirstname(String firstname) {
        put("firstname", firstname);
    }

    public String getLasttname(){
        return getString("lastname");
    }

    public void setLastname(String lastname) {
        put("lastname", lastname);
    }

    public Date getBirthday(){
        return  getDate("birthday");
    }

    public  void setBirthday(Date birthday){
        put("birthday", birthday);
    }

    public UserGender getGender() {
        return UserGender.valueOf(getString("gender"));
    }

    public void setGender(UserGender gender) {
        put("gender", gender.toString());
    }
}
