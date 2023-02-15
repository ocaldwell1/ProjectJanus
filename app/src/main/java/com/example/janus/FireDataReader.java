package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FireDataReader {
    private FirebaseFirestore db;
    private final FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private static FireDataReader fireDataReader;

    private FireDataReader() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fUser = mAuth.getCurrentUser();
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
                            newTask.setId(document.getId());
                        }
                        Collections.sort(taskList, Collections.reverseOrder());
                    }
                });
        return taskList;
    }

    public void addTaskToFireStore(com.example.janus.Task task) {
        String id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Task").document();
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("taskName", task.getName());
        taskMap.put("taskDueDate", task.getDueDate());
        taskMap.put("taskNote", task.getNote());
        taskMap.put("taskWeight", task.getWeight());
        taskMap.put("taskSource", task.getSource());
        taskMap.put("userID", id);
        task.setId(documentReference.getId());
        documentReference.set(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success: Added Task to FireStore");
            }
        });
    }

    public void removeTaskFromFireStore(com.example.janus.Task task) {

        db = FirebaseFirestore.getInstance();
        db.collection("Task").document(task.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public ArrayList<Contact> getContactList() {
        ArrayList<String> contactIds = new ArrayList<>();
        //[wmenkus] Compound query, first gets a list of ids of contacts
        db.collection("Contact").whereEqualTo("user1", mAuth.getCurrentUser())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            contactIds.add(document.getString("user2"));
                        }
                    }
                });
        //[wmenkus] then gets the info from each user with an id on that list
        ArrayList<Contact> contactList = new ArrayList<>();
        for(String id : contactIds) {
            db.collection("User").whereEqualTo("userID", id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String firstName = data.get("userFirstName").toString();
                                String lastName = data.get("userLastName").toString();
                                String email = data.get("userEmail").toString();
                                contactList.add(new Contact(firstName, lastName, email));
                            }
                        }
                    });
        }
        return contactList;
    }

    public void signOut() {
        mAuth.signOut();
    }
}
