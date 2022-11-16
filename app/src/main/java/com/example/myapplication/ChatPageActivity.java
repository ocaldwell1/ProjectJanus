package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ChatActivity> chats;
    private String username = "example1";
    private String username2 = "example2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        recyclerView = findViewById(R.id.recyclerview);
        chats = new ArrayList<>();
        }
        public void onUserClicksChat(){
        //TODO once user clicks chat from arraylist, chat activity will start, transfer username and int pos. w/putExtra

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }
}