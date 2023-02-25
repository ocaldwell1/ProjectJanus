package com.example.janus;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String myUsername, emailOfRoommate, chatroomId;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private TextView chattingWith;
    private ImageView sendIcon;
    private ProgressBar progressBar;
    private ImageView imageToolbar;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private Bundle args;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment;
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //initializes variables to respective xml layout counterparts
        Bundle bundle = getArguments();
        emailOfRoommate = bundle.getString("EMAIL_OF_ROOMMATE");
        myUsername = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        recyclerView = view.findViewById(R.id.recyclerchat);
        messageInput = view.findViewById(R.id.editMessageInput);
        sendIcon = view.findViewById(R.id.sendIcon);
        progressBar = view.findViewById(R.id.progressChat);
        messages= new ArrayList<>();

        //method for when send button is pressed, pushes message to firebase
        // creates doc in messages called chatroom id and sets data to message parameters
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("messages/"+chatroomId+"/messageList").add(
                        new Message(FirebaseAuth.getInstance().getCurrentUser()

                                .getEmail(),
                                messageInput.getText().toString()));


                messageInput.setText(""); //clears previous message after send
            }
        });
        messageAdapter = new MessageAdapter(messages,getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(messageAdapter);
        setUpChatroom(getChatroomId());
    }
    private void setUpChatroom(String chatroomId) {
        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value,
                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        attachMessageListener(chatroomId);
                    }
                });
    }


    private String getChatroomId() {
        String chatroomId;

        // [wmenkus] first gets the number of emails in "emailOfRoommate," which will be multiple if
        // it's a group chat
        int numOfEmails = 0;
        for(int i = 0; i < emailOfRoommate.length(); i++) {
            if(emailOfRoommate.charAt(i) == '@') {
                numOfEmails++;
            }
        }

        // [wmenkus] if there are multiple emails, it's a group chat and already works as a
        // chatroomId
        if(numOfEmails > 2) {
            chatroomId = emailOfRoommate;
            return chatroomId;
        }

        // [wmenkus] otherwise, create a chatroomId by concatenating the user's email with
        // the friend's email in alphabetical order
        if(emailOfRoommate.compareTo(myUsername) > 0) {
            chatroomId = myUsername + emailOfRoommate;
        }
        else if(emailOfRoommate.compareTo(myUsername) == 0) {
            chatroomId = myUsername + emailOfRoommate;
        }
        else{
            chatroomId = emailOfRoommate + myUsername;
        }
        return chatroomId;
    }


    //checks for when messages change/ notifies user of message being received
    // add message from firebase to arraylist on data change
    //Need way to order messages by time
    private void attachMessageListener(String chatroomId) {
        FirebaseFirestore.getInstance().collection("messages/"+chatroomId+"/messageList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {





                        //int size for debug purposes
                        int size =0;
                        for(DocumentChange dc: value.getDocumentChanges()) {

                            messages.add(new Message(dc.getDocument().get("sender").toString(),
                                    dc.getDocument().get("content").toString()));
                            size++;
                                    Log.d("myTag", String.valueOf(size));
                        }

                      //  }


                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messages.size()-1);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                  }
        });
    }

}