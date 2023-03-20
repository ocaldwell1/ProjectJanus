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
    private ArrayList<Task> taskList, newList;
    private NavController navController;
    private CalendarDayTaskAdapter calendarDayTaskAdapter;
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
        taskList = TaskList.getInstance().getTaskList().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Calendar Day");
        return inflater.inflate(R.layout.fragment_calendar_day, container, false);
    }

    // generates a new task arraylist with due dates matching the calendar selected date
    public ArrayList<Task> getNewTaskList(ArrayList<Task> taskLists, String dueDate){
        ArrayList<Task> newTask = new ArrayList<>();

        for(int i = 0; i < taskList.size(); i++) {
            Task task = taskLists.get(i);
            if (eventDate(task.getDueDate(), dueDate).equals(dueDate)) {
                newTask.add(task);
            }
        }
        return newTask;
    }
    // function used to compare task due date to calendar selected date
    public String eventDate(String taskDate, String selectedDate){
        String day, month, year;
        String newDate = "x";
        month = taskDate.substring(0,2);
        day = taskDate.substring(3,5);
        year = taskDate.substring(6,10);
        // converting numerical month to word
        switch (month) {
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
            default:
                break;
        }
        newDate = month + " " + day + ", " + year;
        if(newDate.equals(selectedDate)){
            newDate = selectedDate;
        }
        return newDate;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        date = (TextView) view.findViewById(R.id.calendarDayTextView);
        taskView = view.findViewById(R.id.calendarDayTaskRecyclerView);
        // null object ref error
        date.setText(getArguments().getString("selectedDay"));
        String dateSelected = getArguments().getString("selectedDay");
        newList = getNewTaskList(taskList, dateSelected);
        calendarDayTaskAdapter = new CalendarDayTaskAdapter(newList, dateSelected);
        taskView.setHasFixedSize(true);
        taskView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taskView.setAdapter(calendarDayTaskAdapter);
        calendarDayTaskAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        // The onClick implementation of the RecyclerView item click
        final Task taskSelected = newList.get(position);

        // Send the values of the current card to the next fragment
        Bundle bundle = new Bundle();
        bundle.putString("taskId",taskSelected.getId());
        Navigation.findNavController(view).navigate(R.id.action_calendarDayFragment_to_taskDetailsFragment,bundle);
    }
}