package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Chat> chats;
    private String username = "example1";
    private String username2 = "example2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        recyclerView = findViewById(R.id.recyclerview);
        chats = new ArrayList<>();
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }
}