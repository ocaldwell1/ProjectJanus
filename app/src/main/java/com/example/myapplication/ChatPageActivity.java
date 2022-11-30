package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ChatPageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private ChatPageAdapter chatPageAdapter;
    ChatPageAdapter.OnUserClickListener onUserClickListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerview);
        users = new ArrayList<>();
        onUserClickListener = new ChatPageAdapter.OnUserClickListener() {
            public void OnUserClicked(int position) {
                startActivity(new Intent(ChatPageActivity.this, ChatActivity.class).putExtra("name_of" +
                        "_roommate", users.get(position).getFirstName())
                        .putExtra("email_of_roommate", users.get(position).getEmail()));

            }
        };

        getUsers();

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


    // gets users by querying for docs in users class, gets contents from each doc, converts to java user object
    private void getUsers(){
        //use if arraylist becomes duplicated after each start of app
        // users.clear();

        // supposed to get users from database and convert to user class to add to array list of users
        FirebaseFirestore.getInstance().collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot querySnapshot: value.getDocuments()) {
                    users.add(querySnapshot.toObject(User.class));
                }
                chatPageAdapter = new ChatPageAdapter(users,ChatPageActivity.this, onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager((ChatPageActivity.this)));
                recyclerView.setAdapter((chatPageAdapter));
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

        });
    }
}