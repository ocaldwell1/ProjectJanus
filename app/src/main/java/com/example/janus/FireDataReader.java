package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
}
