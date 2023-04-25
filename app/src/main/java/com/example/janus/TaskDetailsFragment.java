package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.Calendar;
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
        //taskNotesView.setText("" + currentTask.getPriority()); //TODO [wmenkus] replace this with currentTask.getNote(), this has been replaced with getPriority() temporarily for debugging
        taskNotesView.setText("" + currentTask.getNote());
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
                //TaskList.getInstance().getCompletedTaskList();
                // remove
                taskList.removeTask(taskId);
                navController.navigate(R.id.action_taskDetailsFragment_to_taskFragment);
            }
        });

        shareTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShareableLink(view );
                //navController.navigate(R.id.action_taskDetailsFragment_to_editTaskFragment,bundle);
            }
        });
    }

    public void createShareableLink(View view ) {

        // [jms] check for blank tasks
        String taskName = titleNameView.getText().toString();

        // [jms] check for blank task source
        String source = taskSourceView.getText().toString();

        // Check due-date
        String dueDate = currentTask.getDueDate();

        int weight = currentTask.getWeight();
        String notes = taskNotesView.getText().toString();

        String[] dates = dueDate.split("/");
        Uri taskData = Uri.parse("http://www.janus.com/task/").buildUpon()
                .appendPath(taskName)
                .appendPath(source)
                .appendPath("" + weight)
                .appendPath(dates[0])
                .appendPath(dates[1])
                .appendPath(dates[2])
                .appendPath(notes).build();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(taskData));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

}