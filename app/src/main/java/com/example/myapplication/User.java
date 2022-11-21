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

    public User(String first, String last, String email, String pass, String id){
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.id = id;
    }

    public User(String userFirst, String userLast, String userEmail, String userID) {
    }

    public User() {
        mAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        if (fUser == null){

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
        return id;
    }

    public void addUserToFirestore(String firstName, String lastName, String email, String id){
        id = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("User").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("userFirstName", firstName);
        user.put("userLastName", lastName);
        user.put("userEmail", email);
        user.put("userID", id);
    }

    public boolean isLoggedIn(){
        return fUser != null;
    }

    public void addTask(Task task){
        taskList.add(task);
        task.addTaskToFirestore();
    }
    public void removeTask(Task task){
        taskList.remove(task);
        //task.addTaskToFirestore();
    }



}