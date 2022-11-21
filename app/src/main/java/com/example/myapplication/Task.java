package com.example.myapplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Task {
    private String name, note, dueDate, source, id;
    private int weight;
    public FirebaseAuth mAuth;
    public FirebaseUser fUser;
    public FirebaseFirestore db;

    public Task(){

    }
    public Task(String name, String note, String dueDate, String source) {
        this.name = name;
        this.note = note;
        this.dueDate = dueDate;
        this.source = source;
    }

    public void addTaskToFirestore(){
        id = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Task").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("taskName", name);
        user.put("taskDueDate", note);
        user.put("taskNote", note);
        user.put("taskSource", source);
    }

    public void deleteTask(){

    }

    public String getTaskName(){
        return this.name;
    }
    public String getTaskNote(){
        return this.note;
    }
    public String getTaskDueDate(){
        return this.dueDate;
    }
    public String getTaskSource(){
        return this.dueDate;
    }
    public int getTaskWeight(){
        return this.weight;
    }
}
