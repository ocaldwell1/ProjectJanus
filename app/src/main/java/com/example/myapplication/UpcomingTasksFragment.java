package com.example.myapplication;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingTasksFragment extends Fragment implements ItemClickListener  {
    //public class UpcomingTasksFragment extends  AppCompatActivity {
    private RecyclerView recyclerView;
    //DatabaseReference database;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;
    private NavController navController;
    private MainActivity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button addTaskButton;

    public UpcomingTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingTasksFragment newInstance(String param1, String param2) {
        UpcomingTasksFragment fragment = new UpcomingTasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //taskList = new ArrayList<>();
        MainActivity activity = (MainActivity) requireActivity();
        taskList = activity.user.getTaskList();
    }
    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        final Task taskSelected = taskList.get(position);

        // Send the values of the current card to the next fragemnt
        Bundle bundle = new Bundle();
        /**bundle.putString("taskName",taskSelected.getTaskName());
         bundle.putString("taskDueDate",taskSelected.getTaskDueDate());
         bundle.putString("taskSource",taskSelected.getTaskSource());
         bundle.putString("taskNotes",taskSelected.getTaskNote());**/
        bundle.putString("taskName",taskSelected.getTaskName());
        bundle.putString("taskDueDate",taskSelected.getTaskDueDate());
        bundle.putString("taskSource",taskSelected.getTaskSource());
        bundle.putString("taskNotes",taskSelected.getTaskNote());
        Navigation.findNavController(view).navigate(R.id.action_taskFragment_to_taskDetailsFragment,bundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_task, container, false);
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        recyclerView = view.findViewById(R.id.taskRecyclerView);
        recyclerView = view.findViewById(R.id.taskRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new TaskAdapter(taskList));

        // Add listeners to treat the item cards as buttonss

        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setClickListener(this); // bind the listener

        activity = (MainActivity) requireActivity();
        User user = activity.user;
        activity.user.setPosition(recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button addTaskButton = (Button) view.findViewById(R.id.taskFragmentAddTaskButton);
        //final NavController navController = Navigation.findNavController(view);
        NavController navController = Navigation.findNavController(view);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_taskFragment_to_addTaskFragment);
            }
        });
        MainActivity activity = (MainActivity) requireActivity();
        User user = activity.user;
        if(!user.isLoggedIn()){
            navController.navigate(R.id.action_taskFragment_to_menuFragment);
        }
    }



}