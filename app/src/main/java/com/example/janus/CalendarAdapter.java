package com.example.janus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarView>
{
    private final ArrayList<String> daysOfMonth;
    private ArrayList<String> dueDays, eventDates;
    private ArrayList<Integer> colorCodes;
    private final OnItemListener onItemListener;
    private String currentMonth, currentYear;

    public CalendarAdapter(ArrayList<String> daysOfMonth, ArrayList<String> eventDays, ArrayList<String> eventDates, String currentMonth, String currentYear ,ArrayList<Integer> colorCodes, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.dueDays = eventDays;
        this.eventDates = eventDates;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.colorCodes = colorCodes;
        this.onItemListener = onItemListener;

    }

    @NonNull
    @Override
    public CalendarView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_grid, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * .15);
        return new CalendarView(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarView holder, int position)
    {
        holder.calendarDay.setText(daysOfMonth.get(position));
        // execute checkEvent function
        checkEvent(holder, this.dueDays, this.eventDates, this.currentMonth, this.currentYear, this.colorCodes, position);
    }
    // this function compares the event date to the date and position on the calendar
    public void checkEvent(@NonNull CalendarView holder, ArrayList<String> days, ArrayList<String> eventDates, String currentMonth, String currentYear,ArrayList<Integer> colorCodes, int position) {
        // changing color of text that will show event day on the calendar
        for (int m = 0; m < eventDates.size(); m++) {
            for (int i = 0; i < days.size(); i++) {
                // first compares calendar day to event day
                if (daysOfMonth.get(position).equals(eventDates.get(m).substring(0, 2))) {
                    // compares calendar month to event month
                    if (eventDates.get(m).substring(3, 5).equals(currentMonth)) {
                        // compares calendar year to event year
                        if (eventDates.get(m).substring(6, 10).equals(currentYear)) {
                            // if all match, then change number for that day to the task color
                            holder.calendarDay.setTextColor(colorCodes.get(m));
                       }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface OnItemListener
    {
        void onItemClick(int position, String dayText);
    }

}
