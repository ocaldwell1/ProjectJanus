package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String usernameOfRoommate, emailOfRoomate, chatroomId;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private TextView chattingWith;
    private ImageView sendIcon;
    private ProgressBar progressBar;
    private ImageView imageToolbar;
    private ArrayList<Message> messages;
    private Messages messageHolder;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //initializes variables to respective xml layout counterparts
        usernameOfRoommate = getIntent().getStringExtra("username_of_roomate");
        emailOfRoomate = getIntent().getStringExtra("email_of_roommate");
        recyclerView = findViewById(R.id.recyclerchat);
        messageInput = findViewById(R.id.editMessageInput);
        chattingWith = findViewById(R.id.ChattingWith);
        imageToolbar = findViewById(R.id.imageToolbar);
        sendIcon = findViewById(R.id.sendIcon);
        progressBar = findViewById(R.id.progressChat);
        chattingWith.setText(usernameOfRoommate);
        messages= new ArrayList<>();

        //method for when send button is pressed, pushes message to firebase
        // creates doc in messages called chatroom id and sets data to message parameters
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("messages")
                        .document(chatroomId).set(new Message(FirebaseAuth.getInstance().getCurrentUser()
                                .getEmail(), emailOfRoomate, messageInput.getText().toString()
                                ));
                messageInput.setText(""); //clears previous message after send
            }
        });
        messageAdapter = new MessageAdapter(messages,ChatActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        setUpChatroom();
    }
    //placeholder method, needs fixing, supposed to create chatroomid by fetching username?
    private void setUpChatroom() {
        FirebaseFirestore.getInstance().collection("user/"+FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                String myUsername = value.getDocuments().get(0).toString();
                if(usernameOfRoommate.compareTo(myUsername) > 0) {
                    chatroomId = myUsername + usernameOfRoommate;
                }
                else if(usernameOfRoommate.compareTo(myUsername) == 0) {
                    chatroomId = myUsername + usernameOfRoommate;
                }
                    else{
                        chatroomId = usernameOfRoommate + myUsername;
                    }
                    attachMessageListener(chatroomId);
                }
        });
    }
    //checks for when messages change
    private void attachMessageListener(String chatroomId) {
        FirebaseFirestore.getInstance().collection("messages/" + chatroomId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                messages.clear();
                for (DocumentSnapshot querySnapshot: value.getDocuments()) {
                    messages.add(querySnapshot.toObject(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}