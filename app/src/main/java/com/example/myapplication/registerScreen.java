package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class registerScreen extends AppCompatActivity {
    private Button submitButton, backButton;
    private EditText firstName, lastName, password, email;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstName = (EditText) findViewById(R.id.registerScreenFirstNameEditText);
        lastName = (EditText) findViewById(R.id.registerScreenLastNameEditText);
        email = (EditText) findViewById(R.id.registerScreenEmailEditText);
        password = (EditText) findViewById(R.id.registerScreenPasswordEditText);
        backButton = (Button) findViewById(R.id.registerScreenBackButton);


        submitButton = (Button) findViewById(R.id.registerScreenSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerClick();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMenu();
            }
        });
    }

    public void registerClick() {
        String userFirst = firstName.getText().toString().trim();
        String userLast = lastName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();

        if (userFirst.isEmpty()) {
            firstName.setError("First name required!");
            //return;
        }
        if (userLast.isEmpty()) {
            lastName.setError("Last name required!");
            //return;
        }
        if (userEmail.isEmpty()) {
            email.setError("Email required!");
            //return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Email is not valid!");
            email.requestFocus();
            //return;
        }
        if (userPass.isEmpty() || userPass.length() < 8) {
            password.setError("Password length needs to be at least 8 characters");
            password.requestFocus();
            // Add return statement here so all errors will be displayed
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // navigate to register complete fragment
                Toast.makeText(registerScreen.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(registerScreen.this, regComplete.class);
                startActivity(intent);
            }
        });
    }
    // goes back to menu if back button is clicked
    public void backMenu() {
        Intent intent = new Intent(registerScreen.this, MainMenu.class);
        startActivity(intent);
    }

}