package com.example.janus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddTaskFragment extends Fragment {
    public AddTaskFragment() {
        // Required empty public constructor
    }

    public static AddTaskFragment newInstance(String param1, String param2) {
        AddTaskFragment fragment = new AddTaskFragment();
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
        getActivity().setTitle("Add Task");
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        Button addTaskNewTaskButton = (Button) view.findViewById(R.id.addTaskNewTaskButton);
        addTaskNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_addTaskFragment_to_newTaskFragment);
            }
        });

        Button addTaskImportTaskButton = (Button) view.findViewById(R.id.deleteTaskButton);
        addTaskImportTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_addTaskFragment_to_importTaskFragment);
            }
        });

        return view;
    }
}