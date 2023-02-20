package com.example.janus;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String myUsername, usernameOfRoommate, emailOfRoommate, chatroomId;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private TextView chattingWith;
    private ImageView sendIcon;
    private ProgressBar progressBar;
    private ImageView imageToolbar;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                // emailOfRoommate = bundle.getString(EMAIL_OF_ROOMMATE, "unknown");
                //usernameOfRoommate = bundle.getString(EMAIL_OF_ROOMMATE, "unknown");

               // usernameOfRoommate = emailOfRoommate = EMAIL_OF_ROOMMATE = "6";

            }

        }
        //if (bundle != null) {
            //emailOfRoommate = bundle.getString("EMAIL_OF_ROOMMATE", "unknown");
            //usernameOfRoommate = bundle.getString("EMAIL_OF_ROOMMATE", "unknown");


        //} */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //initializes variables to respective xml layout counterparts
        myUsername = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usernameOfRoommate = "test@test.com";
        //emailOfRoommate = getArguments().getString("EMAIL_OF_ROOMMATE", "unknown");
       // usernameOfRoommate = getArguments().getString("EMAIL_OF_ROOMMATE", "unknown");
        recyclerView = view.findViewById(R.id.recyclerchat);
        messageInput = view.findViewById(R.id.editMessageInput);
        chattingWith = view.findViewById(R.id.ChattingWith);
        //imageToolbar = view.findViewById(R.id.imageToolbar);
        sendIcon = view.findViewById(R.id.sendIcon);
        progressBar = view.findViewById(R.id.progressChat);
        //below line is commented out because covered up by message
        //in runtime and also null
        //chattingWith.setText(usernameOfRoommate);
        messages= new ArrayList<>();

        //method for when send button is pressed, pushes message to firebase
        // creates doc in messages called chatroom id and sets data to message parameters
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("messages/"+chatroomId+"/messageList").add(new Message(FirebaseAuth.getInstance().getCurrentUser()
                                .getEmail(), usernameOfRoommate, messageInput.getText().toString()

                        ));
                //messages.add(new Message(FirebaseAuth.getInstance().getCurrentUser()
                       // .getEmail(), usernameOfRoommate, messageInput.getText().toString()));

                messageInput.setText(""); //clears previous message after send
            }
        });
        messageAdapter = new MessageAdapter(messages,getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(messageAdapter);

        setUpChatroom();
    }
    //placeholder method, needs fixing, supposed to create chatroomid by fetching username?
    // doesnt work because both variables are null so will return error when implementing compareto
    private void setUpChatroom() {
        FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value,
                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {

                       /* if(usernameOfRoommate == null && myUsername == null)
                            chatroomId = "0";
                        else if(myUsername == null)
                            chatroomId= "1";
                        else if (usernameOfRoommate == null)
                            chatroomId = "2";
                        else{
                            chatroomId = "4";
                        }*/
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
                                    dc.getDocument().get("content").toString())); size++;
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