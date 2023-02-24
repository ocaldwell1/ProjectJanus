package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        Button shareTaskButton = (Button) view.findViewById(R.id.shareTaskButton);

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

        shareTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShareableJsonLink(view );
                //navController.navigate(R.id.action_taskDetailsFragment_to_editTaskFragment,bundle);
            }
        });
    }

    public void createShareableJsonLink(View view ) {
        // Get all the values of the current task
        String taskName = currentTask.getName();
        String source = currentTask.getSource();
        String dueDate = currentTask.getDueDate();
        String notes = currentTask.getNote();
        int weight = currentTask.getWeight();

        Date now = new Date();
        Date due;
        // Check the date format
        try {
            due = new SimpleDateFormat("MM/dd/yyyy").parse(dueDate);
        } catch (ParseException e) {
            Toast.makeText(getActivity(), "Please enter a valid date following MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Date could not be parsed. ");
            return;
        }

        // Check for invalid input
        if (taskName.equals("")) {
            Toast.makeText(getActivity(), "Please enter a task name", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No title in task");
            return;
        }
        else if (source.equals("")) {
            Toast.makeText(getActivity(), "Please enter a task source. (e.g., CSCE411", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No task source  in task");
            return;
        }
        else if (due.compareTo(now) < 0) {
            Toast.makeText(getActivity(), "Due date has passed!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Invalid time");
            return;
        }

        // If all other checks pass, navigate back to the Home screen
        // Attach task information to the dynamic link
        JSONObject taskJson = new JSONObject();
        try {
            taskJson.put("name", taskName);
            taskJson.put("source", source);
            taskJson.put("dueDate", dueDate);
            taskJson.put("notes", notes);
            taskJson.put("weight", weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add the shareable link to the clipboard
        ClipboardManager clipboard = (ClipboardManager)
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Shareable Link", String.valueOf(taskJson));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Saved task to clipboard!", Toast.LENGTH_SHORT).show();
    }

}