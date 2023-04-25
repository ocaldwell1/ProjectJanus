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
    private Map<String, Object> userData;
    private final FireDataReader fireDataReader;
    private static User user;
    private String imageURL;
    private PriorityCalculator priorityCalculator;
    private String priorityStrategy;
    private static String[] strategies = {"Standard", "Long Tasks"};
    private static MutableLiveData<Boolean> isLoaded;

    /**
     * [wmenkus] current implementation uses a HashMap from the FireDataReader to store the User
     * class instance variables, this is a questionable implementation and could be fixed by using
     * the User's setters in the FireDataReader class to avoid accessing User data members through
     * a secondary data structure
      */
    private User() {
        fireDataReader = FireDataReader.getInstance();
        Log.d("REQUESTS", "creating user");
        isLoaded = new MutableLiveData<>(false);
        userData = fireDataReader.getUserData();
        priorityCalculator = new StandardPriorityCalculator();
        priorityStrategy = "Standard";
        // imageURL = (String) userData.get("imageURL");
    }

    public static User getInstance() {
        if(user != null) {
            return user;
        }
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && user == null) {
            user = new User();
        }
        return user;
    }

    // [wmenkus] used for testing
    private User(FireDataReader fdr) {
        fireDataReader = fdr;
        isLoaded = new MutableLiveData<>(false);
        userData = fireDataReader.getUserData();
        priorityCalculator = new StandardPriorityCalculator();
        priorityStrategy = "Standard";
    }

    // [wmenkus] used for testing
    public static User getInstanceWithMock(FireDataReader fdr) {
        if(fdr.hasUser() && user == null) {
            user = new User(fdr);
        }
        return user;
    }

    public String getImageURL(){
        return userData.get("imageURL").toString();
    }

    public void setImageURL(String val){
        userData.put("imageURL", val);
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
    public PriorityCalculator getPriorityCalculator() {
        return priorityCalculator;
    }

    public void setPriorityCalculator(String method) {
        if(method.equals("Standard")) {
            priorityCalculator = new StandardPriorityCalculator();
            priorityStrategy = method;
            fireDataReader.changeUserPriorityMethod(method);
        }
        else if(method.equals("Long Tasks")) {
            priorityCalculator = new LongTasksPriorityCalculator();
            priorityStrategy = method;
            fireDataReader.changeUserPriorityMethod(method);
        }
        else {
            priorityCalculator = new StandardPriorityCalculator();
            priorityStrategy = "Standard";
            fireDataReader.changeUserPriorityMethod("Standard");
        }
    }

    public int getPriorityMethodIndex() {
        for(int i = 0; i < strategies.length; i++) {
            if(strategies[i].equals(priorityStrategy)) {
                return i;
            }
        }
        return 0;
    }

    public static boolean isNotLoggedIn(){
        return !FireDataReader.getInstance().hasUser();
    }

    public MutableLiveData<Boolean> isLoaded() {
        if(isLoaded == null) {
            isLoaded = new MutableLiveData<>(false);
        }
        return isLoaded;
    }

    public static void setIsLoaded() {
        isLoaded.setValue(true);
    }


    public void signOut() {
        isLoaded.setValue(false);
        user = null;
    }
}