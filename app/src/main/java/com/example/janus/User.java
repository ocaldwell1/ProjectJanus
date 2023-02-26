package com.example.janus;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * [wmenkus] (2/22/2023) This class is having trouble populating its data. It's a synchronization
 * issue with the FireDataReader where in the original implementation the instance variables
 * would attempt to be assigned before the Map had been returned from the FireDataReader, causing
 * all instance variables to be null. I've temporarily replaced its instance variables with the
 * Map that the FireDataReader returns, and all data accesses will directly pull from that Map.
 * TODO fix this once we know how to synchronize with the database.
 */
public class User {

    private String firstName, lastName, email;
    private Map<String, Object> userData;
    private final FireDataReader fireDataReader;
    private static User user;
    private String imageURL = "null";

    private User() {
        fireDataReader = FireDataReader.getInstance();
        Log.d("REQUESTS", "creating user");
        userData = fireDataReader.getUserData();
        Map<String, Object> userData = fireDataReader.getUserData();
        firstName = (String) userData.get("firstName");
        lastName = (String) userData.get("lastName");
        email = (String) userData.get("email");
    }

    public static User getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && user == null) {
            user = new User();
        }
        return user;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String val){
        imageURL = val;
    }

    public String getFirstName(){
        //return this.firstName;
        return userData.get("firstName").toString();
    }
    public String getLastName(){
        //return this.lastName;
        return userData.get("lastName").toString();
    }
    public String getEmail(){
        //return this.email;
        return userData.get("email").toString();
    }

    public static boolean isNotLoggedIn(){
        return !FireDataReader.getInstance().hasUser();
    }

}