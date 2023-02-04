package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
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
        db = FirebaseFirestore.getInstance();
        //TODO fill in the rest of the constructor, how do we get mAuth? how do we get fUser?
    }

    public static FireDataReader getInstance() {
        if(fireDataReader == null) {
            fireDataReader = new FireDataReader();
        }
        return fireDataReader;
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
}
