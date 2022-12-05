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
    private CollectionReference dbc;
    private DocumentReference documentReference;
    private ArrayList <Task> taskList;
    private int taskPosition;

    public User(String first, String last, String email, String id){
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.id = id;
        this.taskPosition = 0;
    }

    public User() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fUser = mAuth.getCurrentUser();

        taskList = new ArrayList<Task>();
        this.taskPosition = 0;

        if (fUser != null){
            String userID = fUser.getUid();
            DocumentReference documentReference = db.collection("User").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            Map<String, Object> user = document.getData();
                            firstName = (String) user.get("userFirstName");
                            lastName = (String) user.get("userLastName");
                            email = (String) user.get("userEmail");
                            id = (String) user.get("userID");

                            Log.d(TAG, "Success: " + firstName);

                            // add taskList functions
                            /* for(QueryDocumentSnapshot document : task.getResult()) {
                            String taskName = data.get("taskName");
                            etc
                            Task newTask = new Task(taskName, taskSource, etc.);
                            taskList.add(newTask);
                            }
                        }
                        */
                        }
                    }
                }
            });

            db.collection("Task").whereEqualTo("userID", userID)
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
                                newTask.setTaskID(document.getId());
                            }
                        }
                    });
            sortTaskList();
        }
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
    public void addUserToFireStore(String firstName, String lastName, String email, String id){
        db = FirebaseFirestore.getInstance();

        documentReference = db.collection("User").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("userFirstName", firstName);
        user.put("userLastName", lastName);
        user.put("userEmail", email);
        user.put("userID", id);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success: Added to FireStore");
            }
        });
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

    public boolean isLoggedIn(){
        return fUser != null;
    }

    public void addTask(Task task) {
        Log.d(TAG, "Success: Added to Task FireStore 1 ");
        taskList.add(task);
        sortTaskList();
        Log.d(TAG, "Success: Added to Task FireStore 2 ");
        task.addTaskToFireStore();
        Log.d(TAG, "Success: Added to Task FireStore 3 ");
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