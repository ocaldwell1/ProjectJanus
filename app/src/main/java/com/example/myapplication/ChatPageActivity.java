package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<ChatActivity> chats;
    private FirebaseAuth mAuth;

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
    }// method that if profile image clicked, starts activity to profile, may not need after merge
    /* public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_profile){
            startActivity(new Intent(ChatPageActivity.this, ProfileActivity));
        }
        return super.onOptionsItemSelected(item);
    }*/
}