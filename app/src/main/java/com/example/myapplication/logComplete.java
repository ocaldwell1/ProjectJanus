package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class logComplete extends AppCompatActivity {
    private Button logoutButton;
    private Button addTaskButton;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcomplete);

        addTaskButton = (Button) findViewById(R.id.logCompleteAddTaskShim);
        logoutButton = (Button) findViewById(R.id.logCompleteLogOutButton);
        mAuth = FirebaseAuth.getInstance();


        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showAddTaskScreen();}
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

    }
    public void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(logComplete.this, MainMenu.class);
        startActivity(intent);
    }
    public void showAddTaskScreen() {
        Intent intent = new Intent(logComplete.this, AddTask.class);
        startActivity(intent);
    }
}