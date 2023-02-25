package com.example.janus;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatPageFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private String queryEmail;
    private ArrayList<Contact> contacts;
    private ArrayList<String> receiverEmails;
    private String concatEmails;
    private ProgressBar progressBar;
    private ChatPageAdapter chatPageAdapter;
    ChatPageAdapter.OnContactClickListener onContactClickListener;
    private NavController navController;
    private Button addFriendButton;
    private Button friendRequestsButton, createGroupChatButton;
    private EditText typeFriendEmail, typeFriendEmailForGroup;
    private ImageView sendIcon;
    private Button done;
    public ChatPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatPage.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatPageFragment newInstance(String param1, String param2) {
        ChatPageFragment fragment = new ChatPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Chat Page");
        return inflater.inflate(R.layout.fragment_chat_page, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerview);
        sendIcon = view.findViewById(R.id.sendIconForChatPage);
        addFriendButton = view.findViewById(R.id.addFriendButton);
        done = view.findViewById(R.id.Done);
        friendRequestsButton = view.findViewById(R.id.chatPageFriendRequestsButton);
        createGroupChatButton = view.findViewById(R.id.createGroupChat);
        contacts = new ArrayList<>();
        receiverEmails = new ArrayList<>();
        typeFriendEmail = view.findViewById(R.id.typeFriendEmail);
        //typeFriendEmailForGroup = view.findViewById(R.id.typeFriendEmailForGroup);
        sendIcon = view.findViewById(R.id.sendIconForChatPage);
        navController = Navigation.findNavController(view);

        friendRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_chatPageFragment_to_friendRequestsFragment);
            }
        });
        createGroupChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIcon.setVisibility(View.VISIBLE);
                typeFriendEmail.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
                typeFriendEmail.requestFocus();
                addFriendButton.setVisibility(View.GONE);
                sendIconListener2();
                doneIconListener();
            }
        });
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //attempts to set typeFriendEmail input to visible on click as well as give focus to it
            public void onClick(View view) {
                //below command attempts to make list of contacts disappear iff add friend button is pressed
                //it disappears, but also makes virtual keyboard hide other bottom buttons like send
                // need to add contraints to other elements in case recycler is invisible?
                // recyclerView.setVisibility(View.GONE);
                typeFriendEmail.setVisibility(View.VISIBLE);
                typeFriendEmail.requestFocus();
                sendIcon.setVisibility(View.VISIBLE);
                sendIconListener();
            }
        });
        onContactClickListener = new ChatPageAdapter.OnContactClickListener() {
            public void OnContactClicked(int position) {
                //bundle is supposed to pass data to ChatFragment
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL_OF_ROOMMATE", contacts.get(position).getEmail());
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(bundle);
                getParentFragmentManager().setFragmentResult("EMAIL_OF_ROOMMATE", bundle);
                Navigation.findNavController(view).navigate(R.id.action_chatPageFragment_to_chatFragment);

            }
        };
        getContacts();



    }
    private void getContacts(){
        //use if arraylist becomes duplicated after each start of app
        contacts.clear();

        // supposed to get contacts from database and convert to contact class to add to array list of contacts that show in phone
        FirebaseFirestore.getInstance().collection("Contact/"+FirebaseAuth.getInstance().getCurrentUser()
                .getEmail()+"/ContactList").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                assert value != null;
                for (int i = 0; i < value.getDocuments().size(); i++) {

                    contacts.add(new Contact(value.getDocuments().get(i).getString("firstName"), value.
                            getDocuments().get(i).getString("lastName"),
                            value.getDocuments().get(i).getString("email"),
                            value.getDocuments().get(i).getBoolean("blocked")));
                }

                    chatPageAdapter = new ChatPageAdapter(contacts, getActivity(), onContactClickListener);
                    recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
                    recyclerView.setAdapter((chatPageAdapter));
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

            }

        });
    }
    private void sendIconListener(){
        // if add friend was clicked, listen for send icon press

            sendIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    queryEmail = typeFriendEmail.getText().toString();
                    if (!queryEmail.isEmpty()) {
                    typeFriendEmail.setText("");
                    if (Objects.equals(queryEmail, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        Toast toast = Toast.makeText(getActivity(), "This is your email", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {

                        FirebaseFirestore.getInstance().collection("User").
                                whereEqualTo("userEmail", queryEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value,
                                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
                                        //query search showed no such email, return no user w/ this email
                                        if (value.isEmpty()) {
                                            Toast toast = Toast.makeText(getActivity(), "Unknown email", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {// checks if previous request was already sent
                                            FirebaseFirestore.getInstance().collection("FriendRequest/" + queryEmail + "/" +
                                                            "friendRequestList").whereEqualTo("receiver", queryEmail).whereEqualTo
                                                            ("sender", FirebaseAuth.getInstance().getCurrentUser().getEmail()).
                                                    addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onEvent(@androidx.annotation.Nullable QuerySnapshot value2,
                                                                            @androidx.annotation.Nullable FirebaseFirestoreException error) {
                                                            if (value2.isEmpty()) {
                                                                Toast toast = Toast.makeText(getActivity(), "Request sent", Toast.LENGTH_SHORT);
                                                                toast.show();
                                                                FirebaseFirestore.getInstance().collection("FriendRequest/" + queryEmail
                                                                        + "/friendRequestList").add(new FriendRequest(
                                                                        FirebaseAuth.getInstance().getCurrentUser().getEmail(), queryEmail));
                                                            } else {
                                                                Toast toast = Toast.makeText(getActivity(), "Request previously sent",
                                                                        Toast.LENGTH_SHORT);
                                                                toast.show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }

                }
                    typeFriendEmail.setVisibility(View.GONE);
                    sendIcon.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });

    }
    private void sendIconListener2(){
        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryEmail = typeFriendEmail.getText().toString();
                if (!queryEmail.isEmpty()) {
                    typeFriendEmail.setText("");
                    if (Objects.equals(queryEmail, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        Toast toast = Toast.makeText(getActivity(), "This is your email", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {

                        FirebaseFirestore.getInstance().collection("Contact/"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+
                                        "/ContactList").
                                whereEqualTo("email", queryEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value,
                                                        @androidx.annotation.Nullable FirebaseFirestoreException error) {
                                        //query search showed no such email, return no friend w/ this email
                                        if (value.isEmpty()) {
                                            Toast toast = Toast.makeText(getActivity(), "Unknown friend", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            receiverEmails.add(queryEmail);
                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Friend added, type another email or click done", Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                    }
                                });
                    }

                }
            }

        });
    }
    private void doneIconListener() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create a new LinkedHashSet and adds receiver emails list to it, which removes duplicates
                //clears original list that way it can add back set to get list with no duplicate emails
                //destroys ordering of list but in this case it doesnt matter
                Set<String> set = new LinkedHashSet<>();
                set.addAll(receiverEmails);
                receiverEmails.clear();
                receiverEmails.addAll(set);

                // No people added before done being pressed
                if(receiverEmails.size() == 0){
                    Toast toast = Toast.makeText(getActivity(), "No names to make group chat", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(receiverEmails.size() == 1) {// Only 1 person added before done being pressed
                    Toast toast = Toast.makeText(getActivity(), "Need at least 3 friends for group chat", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{

                    Toast toast = Toast.makeText(getActivity(), "Groupchat being made", Toast.LENGTH_SHORT);
                    toast.show();
                }
                typeFriendEmail.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                sendIcon.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                addFriendButton.setVisibility(View.VISIBLE);
                receiverEmails.clear();
            }
        });
    }

}