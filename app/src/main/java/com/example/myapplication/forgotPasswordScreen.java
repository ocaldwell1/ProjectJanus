package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class forgotPasswordScreen extends AppCompatActivity {
    private Button forgotEmailButton, backButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        emailEditText = (EditText) findViewById(R.id.forgotPasswordEmailEditText);

        forgotEmailButton = (Button) findViewById(R.id.forgotPassForgotEmailButton);
        forgotEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotEmail();
            }
        });

        backButton = (Button) findViewById(R.id.forgotPassBackButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                backToLog();
            }
        });
    }

    public void forgotEmail(){
        String userEmail = emailEditText.getText().toString();
        if(userEmail.isEmpty()){
            emailEditText.setError("Email required!");
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            emailEditText.setError("Email is not valid!");
            emailEditText.requestFocus();
            return;
        }
        Intent intent = new Intent(this, forgotEmailScreen.class);
        startActivity(intent);
    }

    public void backToLog(){
        Intent back = new Intent(forgotPasswordScreen.this, logScreen.class);
        startActivity(back);
    }
}