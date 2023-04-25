package com.example.janus;

import android.util.Log;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Task implements Comparable<Task>{
    private String name, note, source, dueDate, taskID, isCompleted;
    //private String name, note, source, taskID;
    //private Date dueDate;
    private int weight;

    public Task(String name, String note, int weight, String dueDate, String source) {
        this.name = name;
        this.note = note;
        this.weight = weight;
        this.dueDate = dueDate;
        this.source = source;

        // new Tasks are automatically set to false
        this.isCompleted = "False";
    }

    // overloaded
    public Task(String name, String note, int weight, String dueDate, String source, String isCompleted) {
        this.name = name;
        this.note = note;
        this.weight = weight;
        this.dueDate = dueDate;
        this.source = source;
        this.isCompleted = isCompleted;
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

        if(priority >= 4.0/4) {
            colorInt = 0xFFFF0000;
        }
        else if(priority > 3.0/4) {
            colorInt = 0xFFFF8000;
        }
        else if(priority > 2.0/4) {
            colorInt = 0xFFFFFF00;
        }
        else if(priority > 1.0/4) {
            colorInt = 0xFFC5FF00;
        }
        else {
            colorInt = 0xFF00FF00;
        }

        //TODO replace
        //[wmenkus] this will probably never be replaced, good luck to us all
        return colorInt;
    }

    public double getPriority() throws ParseException {
        PriorityCalculator calculator = User.getInstance().getPriorityCalculator();
        return calculator.calculatePriority(weight, dueDate);
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

    public void edit(String newName, String newSource, int newWeight, String newDueDate, String newNote, String isCompleted) {
        name = newName;
        source = newSource;
        weight = newWeight;
        dueDate = newDueDate;
        note = newNote;
        this.isCompleted = isCompleted;
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
    public void setCompleted() {this.isCompleted = "True"; }
    public void setIncompleted() {this.isCompleted = "False"; }

    public boolean isCompleted() {
        if (this.isCompleted.equals("True")) {
            return true;
        }
        return false;
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
