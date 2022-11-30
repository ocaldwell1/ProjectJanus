package com.example.myapplication;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class    logComplete extends AppCompatActivity {
    private Button logoutButton;
    private Button addTaskButton;
    private Button goToCalendarButton;
    private ScrollView taskScrollView;
    private Button testScrollView1;
    private String userID;
    private FirebaseFirestore db;
    private TextView message;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcomplete);

        addTaskButton = (Button) findViewById(R.id.logCompleteAddTaskShim);
        logoutButton = (Button) findViewById(R.id.logCompleteLogOutButton);
        goToCalendarButton = (Button) findViewById(R.id.logCompleteCalendar);
        taskScrollView = (ScrollView) findViewById(R.id.taskScrollView);
        testScrollView1 = (Button) findViewById(R.id.exampleTask1);
        message = (TextView) findViewById(R.id.logCompleteTextView1);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        displayName();

        // Create a LinearLayout element (placeholder)
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.taskLinearLayout);
        //linearLayout.setOrientation(LinearLayout.VERTICAL);
        // TODO: Add way to dynamically aggregate tasks from DB to the scrollview as buttons
        /** Button button = new Button(this);
         * button.setText("Some text");             // Just add formatted text for now?
         * linearLayout.addView(button);
         **/
        testScrollView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showTaskDetails();}
        });

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
        goToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarScreen();
            }
        });

    }
    public void displayName(){
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("User").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        message.setText("Welcome back, " + value.getString("userFirstName") + "!");
                    }
                }
        );
    }
    public void showTaskDetails() {
        //mAuth.signOut();
        Intent intent = new Intent(logComplete.this, TaskDetails.class);
        startActivity(intent);
    }

    public void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(logComplete.this, MainMenu.class);
        startActivity(intent);
    }
    public void showAddTaskScreen() {
        Intent intent = new Intent(logComplete.this, MainActivity.class);
        startActivity(intent);
    }
    public void showCalendarScreen() {
        Intent intent = new Intent(logComplete.this, CalendarScreen.class);
        startActivity(intent);
    }
}