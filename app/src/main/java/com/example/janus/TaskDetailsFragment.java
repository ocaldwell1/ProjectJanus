package com.example.janus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.ParseException;

public class TaskDetailsFragment extends Fragment {
    TaskList taskList;
    Task currentTask;
    Spinner weightSpinner;
    TextView titleNameView,taskSourceView,taskDueDateView,taskNotesView ;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(String param1, String param2) {
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Task Details");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Get the current selected task
        Bundle bundle = getArguments();
        String taskId = bundle.getString("taskId");
        taskList = TaskList.getInstance();
        currentTask = taskList.getTaskById(taskId);

        // Set title to Task name, Source, Weight, Due Date, Notes, Etc
        titleNameView = (TextView) view.findViewById(R.id.taskTitleName);
        titleNameView.setText(currentTask.getName());
        taskSourceView = (TextView) view.findViewById(R.id.taskSource);
        taskSourceView.setText(currentTask.getSource());
        taskDueDateView = (TextView) view.findViewById(R.id.taskDueDate);
        taskDueDateView.setText(currentTask.getDueDate());
        taskNotesView = (TextView) view.findViewById(R.id.taskNotes);
        try {
            taskNotesView.setText("" + currentTask.getPriority()); //TODO [wmenkus] replace this with currentTask.getNote(), this has been replaced with getPriority() temporarily for debugging
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Button editTaskButton = (Button) view.findViewById(R.id.editTaskButton);
        Button deleteTaskButton = (Button) view.findViewById(R.id.deleteTaskButton);

        NavController navController = Navigation.findNavController(view);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_taskDetailsFragment_to_editTaskFragment,bundle);
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskList taskList = TaskList.getInstance();
                // remove
                taskList.removeTask(taskId);
                navController.navigate(R.id.action_taskDetailsFragment_to_taskFragment);
            }
        });
    }
}