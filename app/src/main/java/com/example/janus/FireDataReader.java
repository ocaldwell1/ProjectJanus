package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireDataReader {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private static FireDataReader fireDataReader;

    private FireDataReader() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fUser = mAuth.getCurrentUser();
        //TODO fill in the rest of the constructor, how do we get mAuth? how do we get fUser?
    }

    public static FireDataReader getInstance() {
        if(fireDataReader == null) {
            fireDataReader = new FireDataReader();
        }
        return fireDataReader;
    }

    public boolean hasUser() {
        return (fUser != null);
    }

    public boolean signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    fUser = mAuth.getCurrentUser();
                }
            }
        });
        return hasUser();
    }

    public void addUserToFireStore(String firstName, String lastName, String email, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("User").document(id);
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

    public Map<String, Object> getUserData() {
        Map<String, Object> userData = new HashMap<>();
        if (hasUser()) {
            String userID = fUser.getUid();
            DocumentReference documentReference = db.collection("User").document(userID);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> user = document.getData();
                            userData.put("firstName", user.get("userFirstName"));
                            userData.put("lastName", user.get("userLastName"));
                            userData.put("email", user.get("userEmail"));
                            userData.put("id", user.get("userID"));
                        }
                    }
                }
            });
        }
        return userData;
    }

    public ArrayList<com.example.janus.Task> getTaskList() {
        ArrayList<com.example.janus.Task> taskList = new ArrayList<>();
        db.collection("Task").whereEqualTo("userID", fUser.getUid())
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
                            com.example.janus.Task newTask = new com.example.janus.Task(taskName, notes, weight, dueDate, taskSource);
                            taskList.add(newTask);
                            Log.i("POSITION", "adding " + taskList.size());
                            newTask.setTaskID(document.getId());
                        }
                        User.sortTaskList(taskList);
                    }
                });
        Log.i("POSITION", "about to sort");
        User.sortTaskList(taskList);
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
        return taskList;
    }
}
