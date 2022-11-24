package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String name;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private TextView chattingWith;
    private ImageView sendIcon;
    private ProgressBar progressBar;
    private ImageView imageToolbar;
    private ArrayList<Message> messages;
    private Messages messageHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //initializes variables to respective xml layout counterparts
        recyclerView = findViewById(R.id.recyclerchat);
        messageInput = findViewById(R.id.editMessageInput);
        chattingWith = findViewById(R.id.ChattingWith);
        imageToolbar = findViewById(R.id.imageToolbar);
        sendIcon = findViewById(R.id.sendIcon);
        progressBar = findViewById(R.id.progressChat);
        chattingWith.setText(name);
        messages= new ArrayList<>();
        //++messageHolder = new Messages(messages,g)
    }
    private void messageListener() {
        //TODO connect to firebase
    }
}