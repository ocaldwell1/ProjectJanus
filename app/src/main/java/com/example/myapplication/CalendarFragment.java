package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String date, m, suffix;
    private CalendarView cv;
    private NavController navController;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        cv = (CalendarView) view.findViewById(R.id.calendarViewFragment);
        Bundle bundle = new Bundle();
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@androidx.annotation.NonNull CalendarView view, int year, int month, int dayOfMonth) {
                switch (month+1){
                    case 1:
                        m = "Jan.";
                        break;
                    case 2:
                        m = "Feb.";
                        break;
                    case 3:
                        m = "March";
                        break;
                    case 4:
                        m = "April";
                        break;
                    case 5:
                        m = "May";
                        break;
                    case 6:
                        m = "June";
                        break;
                    case 7:
                        m = "July";
                        break;
                    case 8:
                        m = "Aug.";
                        break;
                    case 9:
                        m = "Sept.";
                        break;
                    case 10:
                        m = "Oct.";
                        break;
                    case 11:
                        m = "Nov.";
                        break;
                    case 12:
                        m = "Dec.";
                        break;
                    default:
                        break;
                }

                date = m + " " + dayOfMonth + ", " + year;

                bundle.putString("selectedDay", date);
                navController.navigate(R.id.action_calendarFragment_to_calendarDayFragment, bundle);
            }
        });
    }
}