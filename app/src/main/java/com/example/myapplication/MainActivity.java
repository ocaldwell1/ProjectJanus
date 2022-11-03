package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button registerButton;
    private Button logButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        registerButton = (Button) findViewById(R.id.mainRegButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        logButton = (Button) findViewById(R.id.mainLogButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        // if a user is logged in, stays logged in
        if (user != null){
            startActivity(new Intent(MainActivity.this, logComplete.class));
        }
    }
    public void openRegister() {
        // go to register screen
        Intent intent = new Intent(MainActivity.this, registerScreen.class);
        startActivity(intent);
    }
    public void openLogin() {
        // go to log in screen
        Intent intent = new Intent(MainActivity.this, logScreen.class);
        startActivity(intent);
    }
}