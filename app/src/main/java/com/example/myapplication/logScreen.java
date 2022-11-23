package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class logScreen extends AppCompatActivity {
    private TextView login;
    private TextView forgotPass;
    private EditText logEmail, logPass;
    private Button backButton;

    private FirebaseAuth mAuth;
    //private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logscreen);

        login = (TextView) findViewById(R.id.logScreenLogInTextView);
        forgotPass = (TextView) findViewById(R.id.logScreenForgotPassTextView);
        logEmail = (EditText) findViewById(R.id.logScreenEmailEditText);
        logPass = (EditText) findViewById(R.id.logScreenPasswordEditText);
        backButton = (Button) findViewById(R.id.logScreenBackButton);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNow();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMenu();
            }
        });
    }

    public void loginNow() {
        String userEmail = logEmail.getText().toString().trim();
        String userPass = logPass.getText().toString().trim();

        if(userEmail.isEmpty()){
            logEmail.setError("Email required!");
            //return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            logEmail.setError("Email is not valid!");
            logEmail.requestFocus();
            //return;
        }
        if(userPass.isEmpty() || userPass.length() < 8){
            logPass.setError("Password length needs to be at least 8 characters");
            logPass.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(logScreen.this, "Logged in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(logScreen.this, logComplete.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(logScreen.this, "Error! Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void forgotPassword() {
        Intent intent = new Intent(logScreen.this, forgotPasswordScreen.class);
        startActivity(intent);
    }

    public void backMenu() {
        Intent intent = new Intent(logScreen.this, MainMenu.class);
        startActivity(intent);
    }
}
