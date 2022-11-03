package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class regComplete extends AppCompatActivity {
    //private TextView firstName;
    private Button loginButton;
    //private EditText editFirst = (EditText) findViewById(R.id.firstNameText);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regcomplete);
        //Bundle Extra = getIntent().getExtras();
        //String displayFirst = Extra.getString("MessageFirst");

        loginButton =  (Button) findViewById(R.id.regCompleteLogInButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });
    }

    public void logIn(){
        Intent intent = new Intent(this, logScreen.class);
        startActivity(intent);
    }
}