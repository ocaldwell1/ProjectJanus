package com.example.janus;

import android.content.Intent;
import android.net.Uri;
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
    private ArrayList completedTaskList;
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
                    ContactList.getInstance();
                }
            };
            user.isLoaded().observe(getViewLifecycleOwner(), userObserver);
            Log.d("ASYNC", "observing userData loaded is " + user.isLoaded().getValue());

            taskList = TaskList.getInstance();
            completedTaskList = taskList.getCompletedTaskList();
            Log.d("COMTASK", String.valueOf(completedTaskList) );

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
            FriendRequestList.getInstance();
            Log.d("ASYNC", "trying to navigate");
            if(!getActivity().getIntent().getAction().equals("android.intent.action.MAIN")) {
                handleIntent(getActivity().getIntent());
            }
            else {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_taskFragment);
            }
        }
    }

    /**
     * [wmenkus] (4/11/2023) A method to handle the incoming App Link intent, filling a bundle with
     * the relevant information parsed from the link URI and passing it to the newTask screen.
     * This implementation is downright awful and would be one of the first things updated after
     * release. Because the links are generated straight from the Strings of the Task, this breaks
     * if the user even just puts a "/" character in their task name, notes, source, etc. It also
     * means that we need three separate Bundle arguments for the date, since the dates are
     * themselves separated by "/" characters.
     * Fix this in the future by encoding the strings as bytes, and then putting that hexadecimal
     * representation into the URI, decoding once you get to the newTask page.
     * @param intent the Intent generated from the app link
     */
    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String[] taskData = appLinkData.getPath().split("/");
            Bundle bundle = new Bundle();
            System.out.println(taskData[0]);
            System.out.println(taskData[1]);
            bundle.putString("NAME", taskData[2]);
            bundle.putString("SOURCE", taskData[3]);
            bundle.putString("WEIGHT", taskData[4]);
            bundle.putString("MONTH", taskData[5]);
            bundle.putString("DAY", taskData[6]);
            bundle.putString("YEAR", taskData[7]);
            if(taskData.length == 9) {
                bundle.putString("NOTES", taskData[8]);
            }
            Navigation.findNavController(getView()).navigate(R.id.action_startFragment_to_newTaskFragment, bundle);
        }
    }
}