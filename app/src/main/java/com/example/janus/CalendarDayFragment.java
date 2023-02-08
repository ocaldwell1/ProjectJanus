package com.example.janus;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarDayFragment extends Fragment implements ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView date;
    private RecyclerView taskView;
    private ArrayList<Task> taskList;
    private NavController navController;
    User user;
    public CalendarDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarDayFragment newInstance(String param1, String param2) {
        CalendarDayFragment fragment = new CalendarDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskList = TaskList.getInstance().getTaskList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_day, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        date = (TextView) view.findViewById(R.id.calendarDayTextView);
        taskView = view.findViewById(R.id.calendarDayTaskRecyclerView);
        // null object ref error
        date.setText(getArguments().getString("selectedDay"));
        String dateSelected = getArguments().getString("selectedDay");

        taskView.setHasFixedSize(true);
        taskView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taskView.setAdapter(new CalendarDayTaskAdapter(taskList, dateSelected));

    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        final Task taskSelected = taskList.get(position);

        // Send the values of the current card to the next fragment
        Bundle bundle = new Bundle();
        bundle.putString("taskName",taskSelected.getName());
        bundle.putString("taskDueDate",taskSelected.getDueDate());
        bundle.putString("taskSource",taskSelected.getSource());
        bundle.putString("taskNotes",taskSelected.getNote());
        bundle.putString("taskID",taskSelected.getId());
        Navigation.findNavController(view).navigate(R.id.action_calendarDayFragment_to_taskDetailsFragment,bundle);
        user = User.getInstance();
        user.setPosition(position);
    }
}