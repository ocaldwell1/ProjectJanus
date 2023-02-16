package com.example.janus;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String message, day, date, currentMonth, currentYear;
    private NavController navController;
    private TextView monthYearText;
    private ImageButton back, forward;
    private RecyclerView calendarRecyclerView;
    private LocalDate currentDate;
    private ArrayList<Task> taskList;
    private ArrayList<String> taskDates;
    private ArrayList<Integer> colorCodes;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTextView);
        currentDate = LocalDate.now();
        setCalendarView();
        back = (ImageButton) view.findViewById(R.id.imageBackButton);
        forward = (ImageButton) view.findViewById(R.id.imageNextButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonth(v);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonth(v);
            }
        });
    }

    // creates array of task due date
    public ArrayList<String> taskDueDates() {
        // grabbing user task list and adding the due date tasks to array list
        //taskList = TaskList.getInstance().getTaskList();
        ArrayList<String> dates = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            dates.add(task.getDueDate());
           //Log.d(TAG, "Task day is " + eventDay(dates.get(i)));
        }
        return dates;
    }

    // creates array of task colors
    public ArrayList<Integer> taskColorCodes() {
        // grabbing user task list and adding the priority color of tasks to array list
        //taskList = TaskList.getInstance().getTaskList();
        ArrayList<Integer> codes = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            codes.add(task.getPriorityColor());
        }
        return codes;
    }

    private void setCalendarView() {
        monthYearText.setText(monthYearFromDate(currentDate));
        // get the number of days in the current month
        ArrayList<String> daysInMonth = daysInMonthArray(currentDate);

        // defining arrayLists, (when defined outside, i got uninitialized errors)
        ArrayList<String> monthDays = new ArrayList<>();
        ArrayList<String> eventDays = new ArrayList<>();;
        ArrayList<String> eventDates = new ArrayList<>();

        //task due dates array list
        taskDates = taskDueDates();

        // convert current month to string
        int intMonth = currentDate.getMonthValue();
        if(intMonth < 10){
            currentMonth = "0" + intMonth;
        }else {
            currentMonth = String.valueOf(intMonth);
        }

        // convert current year to string
        int intYear = currentDate.getYear();
        currentYear = String.valueOf(intYear);

        // get arraylist of days in the month
        for (int i = 0; i < daysInMonth.size(); i++) {
            day = daysInMonth.get(i);
            if (!day.equals("")) {
                // looks at task dates arraylist to get year
                monthDays.add(day);
                // Log.d(TAG, "monthDays: " + day);
            }
        }
        // obtain date, year, month of event and compare to specific day in month
        for(int x = 0; x < taskDates.size(); x++) {
            date = taskDates.get(x);
            eventDates.add(taskDates.get(x));
            //year = (taskDates.get(x).substring(6, 10));
            //eventYears.add(year);
            //month = (taskDates.get(x).substring(3,5));
            //eventMonths.add(month);
            // check if month day is equal to day of event
            for (int i = 0; i < monthDays.size(); i++) {
                if(monthDays.get(i).equals(date.substring(0,2))){
                    eventDays.add(monthDays.get(i));
                }
            }
        }
        //colorCodes arraylist
        colorCodes = taskColorCodes();

        //Log.d(TAG, "dueDates (setMonth) eventDates : " + eventDates);
        //Log.d(TAG, "dueDates (setMonth) eventDays : " + eventDays);
        // new calendar adapter with the parameters
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, eventDays, eventDates, currentMonth, currentYear, colorCodes, this);
        // 7 columns for each day of the week
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    //  creates array of days in current calendar month
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);
        //Log.d(TAG, "yearMonth: " + yearMonth);

        // getting number of days in month
        int daysInMonth = yearMonth.lengthOfMonth();
        //Log.d(TAG, "dayInMonth: " + daysInMonth);

        // first day
        LocalDate firstDay = currentDate.withDayOfMonth(1);
        //Log.d(TAG, "firstDay: " + firstDay);

        // this returns the day of the week which the 1st of the month will be, range 0-6
        int dayOfWeek = firstDay.getDayOfWeek().getValue();
       // Log.d(TAG, "dayOfWeek: " + dayOfWeek);
       // Log.d(TAG, "daysInMonth+dayOfWeek: " + (daysInMonth + dayOfWeek));

        // creating daysInMonthArray
        for (int i = 1; i <= 38; i++) {
            // if i is outside range
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {

                //Log.d(TAG, "not added: " + i);
                // on calendar, this is the blank spaces before 1st of month and after last day of month
                daysInMonthArray.add("");
            } else {
                // if i is inside the range, begin adding day to daysInMonthArray
                // i - dayOfWeek = day in month
                String dateString = String.valueOf(i - dayOfWeek);
                if ((i - dayOfWeek) < 10) {
                    // single digit days will have a 0 added, this is to compare to dueDate
                    dateString = "0" + (i - dayOfWeek);
                }
                daysInMonthArray.add(dateString);
                //Log.d(TAG, "added: " + i + ", i-dayOfWeek: " + dateString);
            }
        }

        return daysInMonthArray;
    }

    public void previousMonth(View view) {
        this.currentDate = currentDate.minusMonths(1);
        //Log.d(TAG, "minusMonth: " + currentDate.minusMonths(1));
        setCalendarView();
    }

    public void nextMonth(View view) {
        this.currentDate = currentDate.plusMonths(1);
        //Log.d(TAG, "plusMonth: " + this.currentDate.plusMonths(1));
        setCalendarView();
    }

    private String monthYearFromDate(LocalDate date) {
        // Formatting Calendar date MMMM yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private String dateSelected(LocalDate date, String day) {
        // Formatting Calendar date MMMM day yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM " + day + ", yyyy");
        return date.format(formatter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        Bundle bun = new Bundle();

        if (!(dayText.equals(""))) {
            // this is the message that shows on CalendarDay Fragment
            message = dateSelected(currentDate, dayText);
            bun.putString("selectedDay", message);

            //Log.d(TAG, "Selected Day: " + message);
            navController.navigate(R.id.action_calendarFragment_to_calendarDayFragment, bun);
        }
    }

}