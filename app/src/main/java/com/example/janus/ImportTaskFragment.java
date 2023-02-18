package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImportTaskFragment extends Fragment {
    Button saveButton;
    EditText taskLinkText;
    public ImportTaskFragment() {
        // Required empty public constructor
    }

    public static ImportTaskFragment newInstance(String param1, String param2) {
        ImportTaskFragment fragment = new ImportTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        taskLinkText = view.findViewById(R.id.taskLinkText);
        saveButton = (Button) view.findViewById(R.id.importTaskButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    verifyTaskAndAutoPopulate(view);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void verifyTaskAndAutoPopulate(View view ) {
        String jsonInput = taskLinkText.getText().toString();

        try {
            JSONObject jsonObj = new JSONObject(jsonInput);
            // Log.d("JSONT", jsonObj.getString("name"));
            String taskName = jsonObj.getString("name");
            String source = jsonObj.getString("source");
            String dueDate = jsonObj.getString("dueDate");
            String notes = jsonObj.getString("notes");
            int weight = Integer.parseInt(jsonObj.getString("weight"));

            // Just add the task for now, and allow the user the option to delete it in the edit task page.
            Task newTask = new Task(taskName, notes, weight, dueDate,source);
            TaskList taskList = TaskList.getInstance();
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

            // If all other checks pass, navigate back to the Edit screen for final confirmation
            taskList.addTask(newTask);
            Bundle bundle = new Bundle();
            bundle.putString("taskId", newTask.getId());
            Navigation.findNavController(view).navigate(R.id.action_importTaskFragment_to_taskDetailsFragment,bundle);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import_task, container, false);
    }
}