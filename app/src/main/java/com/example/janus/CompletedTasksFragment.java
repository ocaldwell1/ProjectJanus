package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.tools.ant.taskdefs.Parallel;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class CompletedTasksFragment extends Fragment implements ItemClickListener  {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;

    private Button doneButton;
    private Button logOutButton;

    public CompletedTasksFragment() {
        // Required empty public constructor
    }

    public static CompletedTasksFragment newInstance(String param1, String param2) {
        CompletedTasksFragment fragment = new CompletedTasksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        TaskList completedTask = TaskList.getInstance();
        final Task taskSelected = completedTask.getCompletedTask(position);
        Bundle bundle = new Bundle();
        bundle.putString("taskId", taskSelected.getId());

        // Create a dialog to delete the task
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this task forever?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                completedTask.deleteCompletedTask(taskSelected.getId());
                Navigation.findNavController(view).navigate(R.id.completedTasksFragment,bundle);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Completed Tasks");
        return inflater.inflate(R.layout.fragment_completed_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(User.isNotLoggedIn()){
            //Navigation.findNavController(view).navigate(R.id.action_taskFragment_to_menuFragment);
        }
        else {
            doneButton = (Button) view.findViewById(R.id.doneButton);
            recyclerView = view.findViewById(R.id.taskRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            taskList = TaskList.getInstance().getCompletedTaskList();
            if (taskList == null) {
                taskList = new ArrayList<>();
            }

            recyclerView.setAdapter(new CompletedTaskAdapter(taskList));

            // Add listeners to treat the item cards as buttons
            taskAdapter = new TaskAdapter(taskList);
            recyclerView.setAdapter(taskAdapter);
            taskAdapter.setClickListener(this); // bind the listener

            NavController navController = Navigation.findNavController(view);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navController.navigate(R.id.action_completedTasksFragment_to_taskFragment);
                }
            });
        }
    }
}