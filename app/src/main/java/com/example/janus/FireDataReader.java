package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
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
        //contactData = new HashMap<>();
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
        // added this check as there was errors when clicking log in with null inputs
        if(!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        fUser = mAuth.getCurrentUser();
                        Log.d(TAG, "Sign in is successful: fUser: " + fUser);
                        Log.d(TAG, "Sign in is successful: isNotLoggedIn: " + User.isNotLoggedIn());
                        Log.d(TAG, "Sign in is successful: hasUser: " + hasUser());
                    }else{
                        Log.d(TAG, "Sign in is not successful");
                    }
                }
            });
        }
        return hasUser();
    }

    public void signIn(FirebaseUser user) {
        fUser = user;
    }

    public void addUserToFireStore(String firstName, String lastName, String email, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("User").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("userFirstName", firstName);
        user.put("userLastName", lastName);
        user.put("userEmail", email);
        user.put("userID", id);
        user.put("userImage", "default");
        user.put("priorityMethod", "Standard");
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Success: Added to FireStore");
            }
        });
    }

    // change image function
    public void changeImageURL(String URL) {
        if (hasUser()) {
            String userID = fUser.getUid();
            db.collection("User").document(userID)
                    .update("userImage", URL);
        }
    }

    public void changeUserPriorityMethod(String method) {
        if(hasUser()) {
            String userID = fUser.getUid();
            db.collection("User").document(userID)
                    .update("priorityMethod", method);
        }
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
                                    userData.put("imageURL", user.get("userImage"));
                                    User.getInstance().setPriorityCalculator(user.get("priorityMethod").toString());
                                    Log.d("REQUESTS", "user email from database: " + userData.get("email").toString());
                                }
                            }
                            Log.d("REQUESTS", "getUserData here1");
                            User.setIsLoaded();
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
                                newTask.setId(document.getId());

                                try {
                                    String completed = data.get("isCompleted").toString();
                                    if (completed.equals("false")) { // better to do bool comparison than string ...
                                        //newTask.setIncompleted();
                                        taskList.add(newTask);
                                    } else if (completed == null) {
                                        taskList.add(newTask);
                                    }
                                } catch (Exception e){
                                    removeTaskFromFireStore(newTask);
                                    addTaskToFireStore(newTask);
                                }

                            }
                            Collections.sort(taskList, Collections.reverseOrder());
                            Log.d("SYNC", "populated task list");
                            TaskList.setIsLoaded();
                        }
                    });
        return taskList;
    }

    // TODO: Test completed task list [jms]
    public ArrayList<com.example.janus.Task> getCompletedTaskList() {
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
                            newTask.setId(document.getId());

                            // check if there is any isCompleted field; if there is no isCompleted
                            // field, add the field and assume it is still in use.

                            try {
                                String completed = data.get("isCompleted").toString();
                                if (completed.equals("true")) { // better to do bool comparison than string ...
                                    //newTask.setIncompleted();
                                    taskList.add(newTask);
                                }

                            } catch (Exception e){
                                removeTaskFromFireStore(newTask);
                                addTaskToFireStore(newTask);
                            }

                        }
                        Collections.sort(taskList, Collections.reverseOrder());
                        Log.d("SYNC", "populated completed task list");
                        //staskList.setIsLoaded();
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
        taskMap.put("isCompleted", task.isCompleted());
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

    public ArrayList<String> getIMG(ArrayList< String> email) {
        ArrayList<String> imgs = new ArrayList<>();
        for (String emails : email) {
            db.collection("User").whereEqualTo("userEmail", emails)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imgs.add(document.getString("userImage"));
                            }
                        }
                    });
        }
        return imgs;
    }
    /**
     * [wmenkus] (3/21/2023) This may end up causing some synchronization issues, looks like the
     * second part of the compound query may start before the first part completes.
     */
    public ArrayList<Contact> getContactList() {
        ArrayList<String> contactIds = new ArrayList<>();
        //[wmenkus] Compound query, first gets a list of ids of contacts
        db.collection("Contact").whereEqualTo("user1", mAuth.getCurrentUser().getUid())
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
            db.collection("Contact").whereEqualTo("userID", id)
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
        User user = User.getInstance();
        Map<String, Object> senderData = new HashMap<>();
        db.collection("User").whereEqualTo("userEmail", sender)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            senderData.put("firstName", data.get("userFirstName").toString());
                            senderData.put("lastName", data.get("userLastName").toString());
                            senderData.put("email", data.get("userEmail").toString());
                            senderData.put("blocked", "false");
                        }
                        String firstName = senderData.get("firstName").toString();
                        String lastName = senderData.get("lastName").toString();
                        String email = senderData.get("email").toString();
                        boolean blocked = Boolean.parseBoolean(senderData.get("blocked").toString());
                        Contact contact = new Contact(firstName, lastName, email, blocked);

                        //[wmenkus] adds a document with user, read as "current user is now friends with sender"
                        db.collection("Contact/" + user.getEmail() + "/ContactList").add(contact);

                        firstName = user.getFirstName();
                        lastName = user.getLastName();
                        email = user.getEmail();
                        blocked = false;
                        contact = new Contact(firstName, lastName, email, blocked);

                        //[wmenkus] adds a document with sender, read as "sender is now friends with current user"
                        db.collection("Contact/" + sender + "/ContactList").add(contact);
                        //[wmenkus] finally, removes the friend request from the database
                        db.collection("FriendRequest/" + user.getEmail() + "/friendRequestList").whereEqualTo("sender", sender)
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
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> user = document.getData();
                    contactData.put("firstName", user.get("userFirstName"));
                    contactData.put("lastName", user.get("userLastName"));
                    contactData.put("email", user.get("userEmail"));
                    contactData.put("blocked",user.get("blocked"));
                    contactData.put("userImage",user.get("userImage"));
                    Log.d("REQUESTS", "returning contactData of size " + contactData.size());
                    //ContactList.getInstance().addTmpDisplay(contactData);
                }
            }
        });
        Log.d("REQUESTS", "returning contactData of size " + contactData.size());
        return contactData;
    }

    public void removeContactFromFirestore(com.example.janus.Contact contact) {
        String userEmail = User.getInstance().getEmail();
        db.collection("Contact/" + userEmail + "/ContactList").whereEqualTo("email", contact.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                            Log.d(TAG, "Contact successfully deleted!");
                        }
                    }
                });
    };

    public void addGroupChatToMembers(String groupChatName, ArrayList<String> members) {
        for(String member : members) {
            db.collection("Contact/" + member + "/ContactList")
                    .add(new Contact("Group", "Chat", groupChatName, false));
        }
    }

    // forgot password reset email function
    public void forgotPassword(String email){
        mAuth.sendPasswordResetEmail(email);
    }
/*  EH - Retesting the issue, I was not able to register with an already registered email.

    public void checkEmail(String email){  // this function will check if email is used
        db.collection("User").whereEqualTo("userEmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Email has been registered. Cannot use. ");
                        }else{
                            Log.d(TAG, "Email has not been registered. Good to use. ");
                        }
                    }
                });
    }*/

    // reset/update Password function
    public void resetPassword(String email, String oldPass, String newPass) {
        fUser = mAuth.getCurrentUser();
        if (hasUser() && !(email.equals("")) && !(oldPass.equals("")) && !(newPass.equals(""))) {

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
                                        // Plan to implement sign out
                                        //signOut();
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
        //fUser.reload();
    }

    public void signOut() {
        Log.d(TAG, "signOut 0: " + mAuth.getCurrentUser());
        Log.d(TAG, "signOut 1: " + User.isNotLoggedIn());
        mAuth.signOut();
        // this should be null
        fUser = mAuth.getCurrentUser();
        Log.d(TAG, "signOut 2: " + mAuth.getCurrentUser());
        Log.d(TAG, "signOut 3: " + User.isNotLoggedIn());
    }
}
