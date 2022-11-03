package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class logComplete extends AppCompatActivity {
    private Button logoutButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcomplete);

        logoutButton = (Button) findViewById(R.id.logCompleteLogOutButton);
        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }
    public void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(logComplete.this, MainActivity.class);
        startActivity(intent);
    }
}