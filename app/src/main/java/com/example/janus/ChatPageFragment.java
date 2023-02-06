package com.example.janus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private ChatPageAdapter chatPageAdapter;
    ChatPageAdapter.OnUserClickListener onUserClickListener;
    private NavController navController;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_page, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerview);
        users = new ArrayList<>();
        navController = Navigation.findNavController(view);
        onUserClickListener = new ChatPageAdapter.OnUserClickListener() {
            public void OnUserClicked(int position) {
                //bundle is supposed to pass data to ChatFragment
                ChatFragment fragment = new ChatFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name_of_roomate", users.get(position).getFirstName());
                bundle.putString("email_of_roomate", users.get(position).getEmail());
                fragment.setArguments(bundle);

                Navigation.findNavController(view).navigate(R.id.action_chatPageFragment_to_chatFragment) ;

            }
        };
        getUsers();
    }
    private void getUsers(){
        //use if arraylist becomes duplicated after each start of app
        users.clear();

        // supposed to get users from database and convert to user class to add to array list of users
        FirebaseFirestore.getInstance().collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                assert value != null;
                for(int i = 0; i < value.getDocuments().size(); i++) {
                    //first method produced errors, new one seems to solve issues
                   // users.add(querySnapshot.toObject(User.class));
                    users.add(new User(value.getDocuments().get(i).getString("userFirstName"),value.getDocuments().get(i).getString("userFirstName")                            ,  value.getDocuments().get(i).getString("userFirstName"),
                            value.getDocuments().get(i).getString("userFirstName")));

                }
                chatPageAdapter = new ChatPageAdapter(users,getActivity(), onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
                recyclerView.setAdapter((chatPageAdapter));
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

        });
    }
}