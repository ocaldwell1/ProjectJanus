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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

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

    private MainActivity activity;
    int position;
    ArrayList<Task> taskList;
    Task currentTask;
    TextView titleNameView, taskSourceView,taskDueDateView,taskNotesView, taskWeightView;
    Spinner weightSpinner;
    Button saveButton;
    User user;
    public EditTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTaskFragment newInstance(String param1, String param2) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        activity = (MainActivity) requireActivity();
        //User

        // Get the current selected task
        user =  activity.user;
        position = activity.user.getPosition();
        taskList = activity.user.getTaskList();
        currentTask = taskList.get(position);

        // Set title to Task name, Source, Weight, Due Date, Notes, Etc
        titleNameView = (TextView) view.findViewById(R.id.newTaskTaskNameEditText);
        titleNameView.setText(currentTask.getTaskName());
        taskSourceView = (TextView) view.findViewById(R.id.newTaskSourceEditText);
        taskSourceView.setText(currentTask.getTaskSource());
        taskDueDateView = (TextView) view.findViewById(R.id.newTaskDueDateEditText);
        taskDueDateView.setText(currentTask.getTaskDueDate());
        taskNotesView = (TextView) view.findViewById(R.id.newTaskNotesEditText);
        taskNotesView.setText(currentTask.getTaskNote());
        weightSpinner = view.findViewById(R.id.newTaskWeightSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.weights, android.R.layout.simple_spinner_item);
        weightSpinner.setAdapter(adapter);
        weightSpinner.setSelection(currentTask.getTaskWeight());

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
        if(!user.isLoggedIn()){
            navController.navigate(R.id.action_taskFragment_to_menuFragment);
        }
    }

    public void modifyTaskAndNavigateBack(View view) {
        // Modify the current ask according to the values in the fields
        this.currentTask.setName(titleNameView.getText().toString());
        this.currentTask.setSource(taskSourceView.getText().toString());
        this.currentTask.setWeight(Integer.parseInt(weightSpinner.getSelectedItem().toString()));
        this.currentTask.setDueDate(taskDueDateView.getText().toString());
        this.currentTask.setNote( taskNotesView.getText().toString());
        Log.d(TAG, "Success: Modify Task");

        // [JMS] Why are we deleting the task, even though we have just directly modified it?
        activity.user.removeTask(currentTask.getTaskID());
        activity.user.addTask(currentTask);
        activity.user.sortTaskList();

        // TODO: Fix bug for navigating to details: Bundle Does not exist!
        Navigation.findNavController(view).navigate(R.id.action_editTaskFragment_to_taskFragment);

    }
}