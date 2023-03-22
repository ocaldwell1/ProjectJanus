package com.example.janus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
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
    private boolean userDataLoaded = false;
    private boolean taskListLoaded = false;


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
        if(User.isNotLoggedIn()) {
            Navigation.findNavController(view).navigate(R.id.action_startFragment_to_menuFragment);
        }
        else {
            user = User.getInstance();
            final Observer<Boolean> userObserver = new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean bool) {
                    userDataLoaded = bool;
                    tryToContinue(view);
                    Log.d("ASYNC", "user data loaded");
                }
            };
            user.isLoaded().observe(getViewLifecycleOwner(), userObserver);
            Log.d("ASYNC", "observing userData loaded is " + user.isLoaded().getValue());

            taskList = TaskList.getInstance();
            final Observer<Boolean> taskListObserver = new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean bool) {
                    taskListLoaded = bool;
                    tryToContinue(view);
                    Log.d("ASYNC", "task data loaded");
                }
            };
            taskList.isLoaded().observe(getViewLifecycleOwner(), taskListObserver);
            Log.d("ASYNC", "observing taskList loaded is " + taskList.isLoaded().getValue());
            tryToContinue(view);
        }
    }

    public void tryToContinue(View view) {
        if(userDataLoaded && taskListLoaded) {
            Log.d("ASYNC", "trying to navigate");
            Navigation.findNavController(view).navigate(R.id.action_startFragment_to_taskFragment);
        }
    }
}