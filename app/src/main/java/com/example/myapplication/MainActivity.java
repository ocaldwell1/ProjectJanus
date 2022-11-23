package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String name;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private TextView chattingWith;
    private ImageView imageView;
    private ArrayList<Message> messages;
    private Messages messageHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        name = "example1";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //initializes variables to respective xml layout counterparts
        recyclerView = findViewById(R.id.recyclerchat);
        messageInput = findViewById(R.id.editMessageInput);
        chattingWith = findViewById(R.id.ChattingWith);
        imageView = findViewById(R.id.sendIcon);
        chattingWith.setText(name);
        messages= new ArrayList<>();
        //++messageHolder = new Messages(messages,g)
    }
    private void messageListener() {
        //TODO connect to firebase
    }
}