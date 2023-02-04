package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String firstName, lastName, email, id;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore db;
    private FireDataReader fireDataReader;
    private CollectionReference dbc;
    private DocumentReference documentReference;
    private ArrayList <Task> taskList;
    private int taskPosition;
    private static User user;

    private User() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fUser = mAuth.getCurrentUser();
        fireDataReader = FireDataReader.getInstance();
        taskList = new ArrayList<Task>();
        Map<String, Object> userData = fireDataReader.getUserData();
        firstName = (String) userData.get("firstName");
        lastName = (String) userData.get("firstName");
        email = (String) userData.get("email");
        id = (String) userData.get("id");

        //Remove this once we make TaskList class
        db.collection("Task").whereEqualTo("userID", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String taskName = data.get("taskName").toString();
                            String taskSource = data.get("taskSource").toString();
                            int weight = Math.toIntExact( (Long) data.get("taskWeight"));
                            String dueDate = data.get("taskDueDate").toString();
                            String notes = data.get("taskNote").toString();
                            Task newTask = new Task(taskName, notes, weight, dueDate, taskSource);
                            taskList.add(newTask);
                            Log.i("POSITION", "adding " + taskList.size());
                            newTask.setTaskID(document.getId());
                        }
                        sortTaskList();
                    }
                });
        Log.i("POSITION", "about to sort");
        sortTaskList();
        Log.i("POSITION", "test" + taskList.size());
        try {
            for (int i = 0; i < taskList.size(); i++) {
                Log.i("POSITION", ""+ i);
                Log.i("POSITION", "" + taskList.get(i).getPriority());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static User getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser()) {
            user = new User();
        }
        return user;
    }

    public void sortTaskList() {
        Collections.sort(taskList, Collections.reverseOrder());
    }

    // get task list [JMS]
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }

    public void setPosition(int position){this.taskPosition = position;}
    public int getPosition(){return this.taskPosition;}
    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getID(){
        return this.id;
    }

    public void removeUserFromFireStore(User fsUser){
        mAuth = FirebaseAuth.getInstance();
        id = fsUser.getID();
        documentReference = db.collection("User").document(id);
        Map<String, Object> user = new HashMap<>();
        user.remove("userFirstName");
        user.remove("userLastName");
        user.remove("userEmail");
        user.remove("userID");
    }

    public static boolean isLoggedIn(){
        return FireDataReader.getInstance().hasUser();
    }

    public void addTask(Task task) {
        taskList.add(task);
        sortTaskList();
        task.addTaskToFireStore();
    }

    public void removeTask(String id) {
        Task found = null;
        for(Task task : taskList){
            if(task.getTaskID().equals(id)) {
                found = task;
                task.removeTaskFromFireStore();
            }
        }
        taskList.remove(found);
        sortTaskList();
    }



}