package com.example.myapplication;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import androidx.compose.ui.res.ColorResources_androidKt;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Task implements Comparable<Task>{
    private String name, note, source, dueDate;
    private int weight;
    public FirebaseAuth mAuth;
    public FirebaseFirestore db;

    public Task(String name, String note, int weight, String dueDate, String source) {
        this.name = name;
        this.note = note;
        this.weight = weight;
        this.dueDate = dueDate;
        this.source = source;
        mAuth = FirebaseAuth.getInstance();
    }

    public void addTaskToFireStore(){
        String id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Task").document();
        Map<String, Object> user = new HashMap<>();
        user.put("taskName", name);
        user.put("taskDueDate", dueDate);
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

    public int getPriorityColor() {
        double priority = 0;
        try {
            priority = getPriority();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        int colorInt = 0;

        if(priority >= 2 * (4.0/4)) {
            colorInt = 0xFFFF0000;
        }
        else if(priority > 2 * (3.0/4)) {
            colorInt = 0xFFFF8000;
        }
        else if(priority > 2 * (2.0)/4) {
            colorInt = 0xFFFFFF00;
        }
        else if(priority > 2 * (1.0)/4) {
            colorInt = 0xFFC5FF00;
        }
        else {
            colorInt = 0xFF00FF00;
        }

        //TODO replace
        return colorInt;
    }

    public double getPriority() throws ParseException {
        Date now = new Date(System.currentTimeMillis());
        Date due = new SimpleDateFormat("dd/MM/yyyy").parse(dueDate); //duedate - now
        double timeDiff = Math.abs(due.getTime() - now.getTime());
        return weight/(timeDiff/8.64e+7);
    }

    public int compareTo(Task task) {
        int result = 0;
        try {
            if (getPriority() > task.getPriority()) {
                result = 1;
            }
            else {
                result = -1;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.d("PARSE", "Failed to parse string into date");
        }
        return result;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
