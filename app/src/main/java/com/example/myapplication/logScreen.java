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

public class logScreen extends AppCompatActivity {
    private TextView login;
    private TextView forgotpass;
    private EditText logemail, logpass;
    private Button back;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logscreen);


        login = (TextView) findViewById(R.id.logintextclick);
        forgotpass = (TextView) findViewById(R.id.forgottext);
        logemail = (EditText) findViewById(R.id.editLogEmail);
        logpass = (EditText) findViewById(R.id.editLogPass);
        back = (Button) findViewById(R.id.backButton);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNow();
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMenu();
            }
        });
    }

    public void loginNow() {
        String userEmail = logemail.getText().toString().trim();
        String userPass = logpass.getText().toString().trim();

        if(userEmail.isEmpty()){
            logemail.setError("Email required!");
            //return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            logemail.setError("Email is not valid!");
            logemail.requestFocus();
            //return;
        }
        if(userPass.isEmpty() || userPass.length() < 8){
            logpass.setError("Password length needs to be at least 8 characters");
            logpass.requestFocus();
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
        Intent intent = new Intent(logScreen.this, MainActivity.class);
        startActivity(intent);
    }
}
