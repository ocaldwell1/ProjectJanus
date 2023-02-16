package com.example.janus;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Task implements Comparable<Task>{
    private String name, note, source, dueDate, taskID;
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
        Date due = new SimpleDateFormat("MM/dd/yyyy").parse(dueDate); //duedate - now
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

    public void edit(String newName, String newSource, int newWeight, String newDueDate, String newNote) {
        name = newName;
        source = newSource;
        weight = newWeight;
        dueDate = newDueDate;
        note = newNote;
    }

    public String getName(){
        return this.name;
    }
    public String getNote(){
        return this.note;
    }
    public String getDueDate(){
        return this.dueDate;
    }
    public String getSource(){
        return this.source;
    }
    public String getId(){
        return this.taskID;
    }
    public int getWeight(){
        return this.weight;
    }
    public void setId(String id){
        this.taskID = id;
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
