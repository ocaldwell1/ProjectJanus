package com.example.janus;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<Map<String, Object>> userData;
    private final FireDataReader fireDataReader;
    private static User user;
    private String imageURL = "null";

    private User() {
        fireDataReader = FireDataReader.getInstance();
        Log.d("REQUESTS", "creating user");
        userData = new MutableLiveData<>();
        userData.setValue(fireDataReader.getUserData());
        // imageURL = (String) userData.get("imageURL");
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
        return userData.getValue().get("firstName").toString();
    }
    public String getLastName(){
        //return this.lastName;
        return userData.getValue().get("lastName").toString();
    }
    public String getEmail(){
        //return this.email;
        return userData.getValue().get("email").toString();
    }

    public static boolean isNotLoggedIn(){
        return !FireDataReader.getInstance().hasUser();
    }

    public MutableLiveData<Map<String, Object>> getUserData() {
        if(userData == null) {
            userData = new MutableLiveData<>();
        }
        return userData;
    }

}