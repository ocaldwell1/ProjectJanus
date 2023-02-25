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
        // Inflate the layout for this fragment
        getParentFragmentManager().setFragmentResultListener("EMAIL_OF_ROOMMATE", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@androidx.annotation.NonNull String requestKey, @androidx.annotation.NonNull Bundle result) {
                emailOfRoommate = result.getString("EMAIL_OF_ROOMMATE");
            }
        });
        return inflater.inflate(R.layout.fragment_chat, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //initializes variables to respective xml layout counterparts
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
                                .getEmail(), emailOfRoommate, messageInput.getText().toString(), Timestamp.now()

                        ));


                messageInput.setText(""); //clears previous message after send
            }
        });
        messageAdapter = new MessageAdapter(messages,getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(messageAdapter);

        setUpChatroom();
    }
    private void setUpChatroom() {
        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value,
                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {


                        if(emailOfRoommate.compareTo(myUsername) > 0) {
                            chatroomId = myUsername + emailOfRoommate;
                        }
                        else if(emailOfRoommate.compareTo(myUsername) == 0) {
                            chatroomId = myUsername + emailOfRoommate;
                        }
                        else{
                            chatroomId = emailOfRoommate + myUsername;
                        }
                        attachMessageListener(chatroomId);
                    }
                });
    }

    //checks for when messages change/ notifies user of message being recieved
    //trying to add message from firebase to arraylist on data cahnge?
    private void attachMessageListener(String chatroomId) {
        FirebaseFirestore.getInstance().collection("messages/"+chatroomId+"/messageList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {




                    //  @Override
                   // public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value,
                                      //  @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        // may need if other chatrooms messages still show after bundle fix
                        //messages.clear();
                        //int size for debug purposes
                        int size =0;
                        for(DocumentChange dc: value.getDocumentChanges()) {
                            messages.add(new Message(dc.getDocument().get("sender").toString(), dc.getDocument().get("receiver").toString(),
                                    dc.getDocument().get("content").toString(),
                                    (Timestamp) dc.getDocument().get("timestamp"))); size++;
                                    Log.d("myTag", String.valueOf(size));
                        }

                      //  }


                       // for (DocumentSnapshot querySnapshot: value) {
                           // messages.add(querySnapshot.toObject(Message.class));
                       // messages.add(new Message(FirebaseAuth.getInstance().getCurrentUser().toString(),
                               // usernameOfRoommate, FirebaseFirestore.getInstance().
                              //  collection("messages").document(chatroomId).toString()));
                      //  }
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messages.size()-1);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                  }
        });
    }

}