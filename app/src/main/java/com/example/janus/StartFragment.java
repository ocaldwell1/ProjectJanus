package com.example.janus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class StartFragment extends Fragment {

    private User user;
    private TaskList taskList;
    private ContactList contactList;
    private boolean userDataLoaded = false;
    private boolean taskListLoaded = false;
    private boolean contactListLoaded = false;


    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getInstance();
        final Observer<Map<String, Object>> userObserver = new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
                userDataLoaded = true;
                Log.d("ASYNC", "user data loaded");
            }
        };
        user.getUserData().observe(this, userObserver);
        Log.d("ASYNC", "observing userData " + user.getUserData().getValue().toString());

        taskList = TaskList.getInstance();
        final Observer<ArrayList<Task>> taskListObserver = new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(ArrayList<Task> taskArrayList) {
                taskListLoaded = true;
                Log.d("ASYNC", "task data loaded");
            }
        };
        taskList.getTaskList().observe(this, taskListObserver);
        Log.d("ASYNC", "observing taskList " + taskList.getTaskList().getValue().toString());

        contactList = ContactList.getInstance();
        final Observer<ArrayList<Contact>> contactListObserver = new Observer<ArrayList<Contact>>() {
            @Override
            public void onChanged(ArrayList<Contact> contacts) {
                contactListLoaded = true;
                Log.d("ASYNC", "contact data loaded");
            }
        };
        contactList.getContactList().observe(this, contactListObserver);
        Log.d("ASYNC", "observing contactList " + contactList.getContactList().getValue().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Start");
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tryToContinue(view);
    }

    public void tryToContinue(View view) {
        if(userDataLoaded && taskListLoaded && contactListLoaded) {
            Navigation.findNavController(view).navigate(R.id.action_startFragment_to_taskFragment);
        }
    }
}