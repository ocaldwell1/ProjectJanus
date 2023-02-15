package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TaskList taskList;
    Task currentTask;
    TextView titleNameView, taskSourceView,taskDueDateView,taskNotesView, taskWeightView;
    Spinner weightSpinner;
    Button saveButton;
    User user;
    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance(String param1, String param2) {
        EditTaskFragment fragment = new EditTaskFragment();
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
        getActivity().setTitle("Edit Task");
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        Button newTaskSaveButton = (Button) view.findViewById(R.id.newTaskSaveButton);
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_addTaskFragment_to_newTaskFragment);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);

        // Get the current selected task
        Bundle bundle = getArguments();
        String taskId = bundle.getString("taskId");
        taskList = TaskList.getInstance();
        currentTask = taskList.getTaskById(taskId);

        // Set title to Task name, Source, Weight, Due Date, Notes, Etc
        titleNameView = (TextView) view.findViewById(R.id.newTaskTaskNameEditText);
        titleNameView.setText(currentTask.getName());
        taskSourceView = (TextView) view.findViewById(R.id.newTaskSourceEditText);
        taskSourceView.setText(currentTask.getSource());
        taskDueDateView = (TextView) view.findViewById(R.id.newTaskDueDateEditText);
        taskDueDateView.setText(currentTask.getDueDate());
        taskNotesView = (TextView) view.findViewById(R.id.newTaskNotesEditText);
        taskNotesView.setText(currentTask.getNote());
        weightSpinner = view.findViewById(R.id.newTaskWeightSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.weights, android.R.layout.simple_spinner_item);
        weightSpinner.setAdapter(adapter);
        weightSpinner.setSelection(currentTask.getWeight());

        // Save button
        saveButton = (Button) view.findViewById(R.id.newTaskSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                try {
                    modifyTaskAndNavigateBack(view);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Check user logged in
        if(User.isNotLoggedIn()){
            navController.navigate(R.id.action_taskFragment_to_menuFragment);
        }
    }

    public void modifyTaskAndNavigateBack(View view) {
        TaskList taskList = TaskList.getInstance();

        /**
         * Saves the current task filled in the form and returns to the main menu page home
         */

        // [jms] check for blank tasks
        String taskName = titleNameView.getText().toString();

        // [jms] check for blank task source
        String source = taskSourceView.getText().toString();

        // Check due-date
        String dueDate = taskDueDateView.getText().toString();

        int weight = Integer.parseInt(weightSpinner.getSelectedItem().toString());
        String notes =  taskNotesView.getText().toString();

        Date now = new Date();
        Date due;
        // Check the date format
        try {
            due = new SimpleDateFormat("MM/DD/YYYY").parse(dueDate);
        } catch (Exception e) {
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

        // TODO: Check for duplicate assignment title/class? Make distinction between due dates?

        // If all other checks pass, navigate back to the Home screen
        Log.d(TAG, "Here");
        taskList.editTask(currentTask.getId(), taskName, notes, weight, dueDate,source);
        Log.d(TAG, "Success: Modify Task");

        // TODO: Fix bug for navigating to details: Bundle Does not exist!
        Navigation.findNavController(view).navigate(R.id.action_editTaskFragment_to_taskFragment);

    }
}