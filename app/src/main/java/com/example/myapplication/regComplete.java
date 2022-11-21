package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;


public class regComplete extends AppCompatActivity {
    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regcomplete);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        message = (TextView) findViewById(R.id.regCompleteMessageTextView);


        displayName();

        loginButton =  (Button) findViewById(R.id.regCompleteLogInButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
    }

    public void displayName(){
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("User").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        message.setText("Thanks for registering " + value.getString("userFirstName") + " " + value.getString("userLastName") + "!");
                        // sign out user
                        mAuth.signOut();
                    }
                }
        );
    }

    // click log in button to go to log in screen
    public void logIn(){
        Intent intent = new Intent(regComplete.this, logScreen.class);
        startActivity(intent);
    }
}