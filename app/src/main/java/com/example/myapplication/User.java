package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String firstName, lastName, email, id;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore db;
    private ArrayList <Task> taskList;

    public User(String first, String last, String email, String id){
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.id = id;
    }

    /*public User(String userFirst, String userLast, String userEmail, String userID) {
    }*/

    public User() {
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        if (fUser != null){
            firstName = "test";
            lastName = "user";
            email = "anEmail";
            id = "anId";
        }
    }



    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String firstName) {
        this.lastName = lastName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String firstName) {
        this.email = email;
    }
    public String getID(){
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        return id;
    }

    public void addUserToFirestore(User fsUser){
        id = fsUser.getID();
        DocumentReference documentReference = db.collection("User").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("userFirstName", fsUser.getFirstName());
        user.put("userLastName", fsUser.getLastName());
        user.put("userEmail", fsUser.getEmail());
        user.put("userID", fsUser.getID());
    }
    public void removeUserFromFirestore(User fsUser){
        mAuth = FirebaseAuth.getInstance();
        id = fsUser.getID();
        DocumentReference documentReference = db.collection("User").document(id);
        Map<String, Object> user = new HashMap<>();
        user.remove("userFirstName");
        user.remove("userLastName");
        user.remove("userEmail");
        user.remove("userID");
    }

    public boolean isLoggedIn(){
        return fUser != null;
    }

    public void addTask(Task task) {
        taskList.add(task);
        task.addTaskToFirestore();
    }

    public void removeTask(Task task) {
        taskList.remove(task);
        // task.removeTaskFromFirestore();
    }



}