package com.example.janus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestsFragment extends Fragment {
    private TextView emptyListText;
    private RecyclerView recyclerView;
    private TaskAdapter friendRequestAdapter;
    private ArrayList<FriendRequest> friendRequestList;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }

    public static FriendRequestsFragment newInstance(String param1, String param2) {
        return new FriendRequestsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_requests, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emptyListText = view.findViewById(R.id.friendRequestEmptyText);
        recyclerView = view.findViewById(R.id.friendRequestRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        friendRequestList = FriendRequestList.getInstance().getFriendRequests();
        if(friendRequestList.isEmpty()) {
            emptyListText.setVisibility(View.VISIBLE);
        }
        Log.d("REQUESTS", "friendRequestList size: " + friendRequestList.size());
        recyclerView.setAdapter(new FriendRequestAdapter(friendRequestList));
    }
}