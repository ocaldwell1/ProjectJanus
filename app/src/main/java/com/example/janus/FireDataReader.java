package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
        if(!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Sign in is successful");
                        fUser = mAuth.getCurrentUser();
                    }else{
                        Log.d(TAG, "Sign in is not successful");
                    }
                }
            });
        }
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
            db.collection("User").document(userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                    Log.d("REQUESTS", "user email from database: " + userData.get("email").toString());
                                }
                            }
                            Log.d("REQUESTS", "getUserData here1");
                        }
                    });
            Log.d("REQUESTS", "getUserData here2");
        }
        Log.d("REQUESTS", "returning function");
        Log.d("REQUESTS", "userData size: " + userData.size());
        return userData;
    }

    public ArrayList<com.example.janus.Task> getTaskList() {
        ArrayList<com.example.janus.Task> taskList = new ArrayList<>();
            db.collection("Task").whereEqualTo("userID", fUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String taskName = data.get("taskName").toString();
                                String taskSource = data.get("taskSource").toString();
                                int weight = Math.toIntExact((Long) data.get("taskWeight"));
                                String dueDate = data.get("taskDueDate").toString();
                                String notes = data.get("taskNote").toString();
                                com.example.janus.Task newTask = new com.example.janus.Task(taskName, notes, weight, dueDate, taskSource);
                                taskList.add(newTask);
                                newTask.setId(document.getId());
                            }
                            Collections.sort(taskList, Collections.reverseOrder());
                            Log.d("SYNC", "populated task list");
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

    /**
     * [wmenkus] This is not right at the moment (2/20/2023), it's currently trying to get isBlocked
     * from the User database, and queries Contact using user IDs. Solve that issue (Maybe
     * User.getInstance.getEmail?) and figure out a way to store tuples of contactEmail,
     * isBlocked in the contactIds list
     */
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
                                boolean isBlocked = Boolean.parseBoolean(data.get("isBlocked").toString());
                                contactList.add(new Contact(firstName, lastName, email, isBlocked));
                            }
                        }
                    });
        }
        return contactList;
    }

    /**
     * [wmenkus] getFriendRequests may require some significant refactoring on the database side, as current
     * implementation uses emails in the database which are harder to extract than the readily available User ID and require
     * an instance of the user. FriendRequestList must be constructed *after* the user but *before* the FriendRequestsFragment is reached.
     */
    public ArrayList<FriendRequest> getFriendRequests() { //TODO remove logs after debugging
        ArrayList<FriendRequest> requestList = new ArrayList<>();
        String userEmail = User.getInstance().getEmail();
        Log.d("REQUESTS", "User email: " + userEmail);
        db.collection("FriendRequest/" + userEmail + "/friendRequestList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("REQUESTS", "database query results: " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String sender = document.get("sender").toString();
                            FriendRequest newRequest = new FriendRequest(sender, userEmail);
                            requestList.add(newRequest);
                        }
                    }
                });
        return requestList;
    }

    public void acceptFriendRequest(String sender) {
        //[wmenkus] first, adds information to the Contact database so that both users are registered as friends of each other
        String userEmail = User.getInstance().getEmail();
        //[wmenkus] adds a document with user first and then sender, read as "current user is now friends with sender"
        DocumentReference documentReference = db.collection("Contact").document();
        Map<String, Object> contactMap = new HashMap<>();
        contactMap.put("user1", userEmail);
        contactMap.put("user2", sender);
        contactMap.put("isBlocked", false);
        documentReference.set(contactMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success: current user is now friends with sender");
            }
        });
        //[wmenkus] then adds a document with sender first and then user, read as "sender is now friends with current user"
        documentReference = db.collection("Contact").document();
        contactMap = new HashMap<>();
        contactMap.put("user1", sender);
        contactMap.put("user2", userEmail);
        contactMap.put("isBlocked", false);
        documentReference.set(contactMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success: sender is now friends with current user");
            }
        });

        //[wmenkus] finally, removes the friend request from the database
        db.collection("FriendRequest/" + userEmail + "/friendRequestList").whereEqualTo("sender", sender)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    }
                });
    }

    public void denyFriendRequest(String sender) {
        String userEmail = User.getInstance().getEmail();
        db.collection("FriendRequest/" + userEmail + "/friendRequestList").whereEqualTo("sender", sender)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    }
                });
    }

    public Map<String, Object> getContactData(String email) {
        Map<String, Object> contactData = new HashMap<>();
        String userID = fUser.getUid();
        db.collection("User").whereEqualTo("userEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        Map<String, Object> user = document.getData();
                        contactData.put("firstName", user.get("userFirstName"));
                        contactData.put("lastName", user.get("userLastName"));
                        contactData.put("email", user.get("userEmail"));
                    }
                }
            }
        });
        return contactData;
    }

    // forgot password reset email function
    public void forgotPassword(){

    }

    // reset/update Password function
    public void resetPassword(String email, String oldPass, String newPass) {
        fUser = mAuth.getCurrentUser();
        User user = User.getInstance();
        if (!(email.equals("")) && !(oldPass.equals("")) && !(newPass.equals(""))) {

            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);
            Log.d(TAG, "credential: " + credential);
            // asks user for credential
            fUser.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // if successful, update the password
                        fUser.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "ReAuthentication and Password update success.");
                                        signOut();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Password update failed.");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "ReAuthentication failed. Incorrect password.");
                    }
                });
        }
    }

    public void signOut() {
        mAuth.signOut();
        fUser = null;
    }
}
