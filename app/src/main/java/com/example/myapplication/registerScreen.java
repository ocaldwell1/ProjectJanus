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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerScreen extends AppCompatActivity {
    private Button submitButton, backButton;
    private EditText firstName, lastName, password, email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

        if(userFirst.isEmpty()){
            firstName.setError("First name required!");
            //return;
        }
        if(userLast.isEmpty()){
            lastName.setError("Last name required!");
            //return;
        }
        if(userEmail.isEmpty()){
            email.setError("Email required!");
            //return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("Email is not valid!");
            email.requestFocus();
            //return;
        }
        if(userPass.isEmpty() || userPass.length() < 8){
            password.setError("Password length needs to be at least 8 characters");
            password.requestFocus();
            // Add return statement here so all errors will be displayed
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(registerScreen.this, "Registered!", Toast.LENGTH_SHORT).show();
                    // sendMessage = new Intent(MainActivity2.this, MainActivity4.class);
                    // sendMessage.putExtra("MessageFirst", messageFirst);
                    // startActivity(sendMessage);
                    // this will go to next registration complete
                    startActivity(new Intent(registerScreen.this, regComplete.class));
                }
            }
        });
    }
    // goes back to menu if back button is clicked
    public void backMenu() {
        Intent intent = new Intent(registerScreen.this, MainMenu.class);
        startActivity(intent);
    }

}