package com.example.myapplication;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class Task {
    private String name, note, source, id, dueDate;
    private int weight;
    public FirebaseAuth mAuth;
    public FirebaseUser fUser;
    public FirebaseFirestore db;

    public Task(){

    }
    public Task(String name, String note, int weight, String dueDate, String source) {
        this.name = name;
        this.note = note;
        this.dueDate = dueDate;
        this.source = source;
        mAuth = FirebaseAuth.getInstance();
    }

    public void addTaskToFireStore(){
        id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Task").document();
        Map<String, Object> user = new HashMap<>();
        user.put("taskName", name);
        user.put("taskDueDate", note);
        user.put("taskNote", note);
        user.put("taskWeight", weight);
        user.put("taskSource", source);
        user.put("userID", id);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success: Added Task to FireStore");
            }
        });
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
        return this.source;
    }
    public int getTaskWeight(){
        return this.weight;
    }
}
