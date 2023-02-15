package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UpcomingTasksFragment extends Fragment implements ItemClickListener  {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    private Button addTaskButton;
    private Button logOutButton;

    public UpcomingTasksFragment() {
        // Required empty public constructor
    }

    public static UpcomingTasksFragment newInstance(String param1, String param2) {
        UpcomingTasksFragment fragment = new UpcomingTasksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        final Task taskSelected = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("taskId", taskSelected.getId());
        Navigation.findNavController(view).navigate(R.id.action_taskFragment_to_taskDetailsFragment,bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(User.isNotLoggedIn()){
            Navigation.findNavController(view).navigate(R.id.action_taskFragment_to_menuFragment);
        }
        else {
            Log.d(TAG, "Looking at the tasks.");
            taskList = TaskList.getInstance().getTaskList();
            Log.d(TAG, String.valueOf(taskList.size()));
            Button addTaskButton = (Button) view.findViewById(R.id.taskFragmentAddTaskButton);
            logOutButton = (Button) view.findViewById(R.id.taskFragmentLogOutButton);
            recyclerView = view.findViewById(R.id.taskRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(new TaskAdapter(taskList));

            // Add listeners to treat the item cards as buttons
            taskAdapter = new TaskAdapter(taskList);
            recyclerView.setAdapter(taskAdapter);
            taskAdapter.setClickListener(this); // bind the listener

            NavController navController = Navigation.findNavController(view);
            addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navController.navigate(R.id.action_taskFragment_to_addTaskFragment);
                }
            });
            logOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FireDataReader.getInstance().signOut();
                    Toast.makeText(getActivity(), "Logged out!", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_taskFragment_to_menuFragment);
                }
            });
        }
    }
}