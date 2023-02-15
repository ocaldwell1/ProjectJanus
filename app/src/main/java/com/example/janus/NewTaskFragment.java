package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class NewTaskFragment extends Fragment {

    private EditText taskNameEditText;
    private EditText sourceEditText;
    private Spinner weightSpinner;
    private EditText dueDateEditText;
    private EditText notesEditText;
    private Button saveButton;

    public NewTaskFragment() {
        // Required empty public constructor
    }

    public static NewTaskFragment newInstance(String param1, String param2) {
        NewTaskFragment fragment = new NewTaskFragment();
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
        getActivity().setTitle("New Task");
        return inflater.inflate(R.layout.fragment_new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        taskNameEditText = view.findViewById(R.id.newTaskTaskNameEditText);
        sourceEditText = view.findViewById(R.id.newTaskSourceEditText);
        weightSpinner = view.findViewById(R.id.newTaskWeightSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.weights, android.R.layout.simple_spinner_item);
        weightSpinner.setAdapter(adapter);
        dueDateEditText = view.findViewById(R.id.newTaskDueDateEditText);
        notesEditText = view.findViewById(R.id.newTaskNotesEditText);
        saveButton = (Button) view.findViewById(R.id.newTaskSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveTaskAndNavigateBack(view);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveTaskAndNavigateBack(View view) {
        /**
         * Saves the current task filled in the form and returns to the main menu page home
         */

        // [jms] check for blank tasks
        String taskName = taskNameEditText.getText().toString();

        // [jms] check for blank task source
        String source = sourceEditText.getText().toString();

        // Check due-date
        String dueDate = dueDateEditText.getText().toString();

        int weight = Integer.parseInt(weightSpinner.getSelectedItem().toString());
        String notes = notesEditText.getText().toString();
        Task newTask = new Task(taskName, notes, weight, dueDate,source);
        TaskList taskList = TaskList.getInstance();

        Date now = new Date();
        Date due;
        // Check the date format
        try {
            due = new SimpleDateFormat("MM/dd/yyyy").parse(dueDate);
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
        taskList.addTask(newTask);
        Log.d(TAG, "Success: Add Task");
        Navigation.findNavController(view).navigate(R.id.action_newTaskFragment_to_taskFragment);

    }
}