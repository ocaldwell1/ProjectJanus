package com.example.janus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class User {

    private final String firstName, lastName, email;
    private final FireDataReader fireDataReader;
    private int taskPosition;
    private static User user;

    private User() {
        fireDataReader = FireDataReader.getInstance();
        Map<String, Object> userData = fireDataReader.getUserData();
        firstName = (String) userData.get("firstName");
        lastName = (String) userData.get("firstName");
        email = (String) userData.get("email");
    }

    public static User getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && user == null) {
            user = new User();
        }
        return user;
    }

    // get task list [JMS]
    public void setPosition(int position){this.taskPosition = position;}
    public int getPosition(){return this.taskPosition;}
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){
        return this.email;
    }

    public static boolean isNotLoggedIn(){
        return !FireDataReader.getInstance().hasUser();
    }

}