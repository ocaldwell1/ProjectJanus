package com.example.janus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // make simpler
        int currentColor = 0;
        String currentDate = "";
        for (int m = 0; m < eventDates.size(); m++) {
            for (int i = 0; i < days.size(); i++) {
                if ((daysOfMonth.get(position) != "")) {
                    // as loop goes through each day of month, get the date and compare to event due date
                    currentDate = currentMonth + "/" + daysOfMonth.get(position) + "/" + currentYear;
                    if (currentDate.equals(eventDates.get(m))) {
                        // if current date matches event due date
                        // change color to highest priority color for that day
                        // this checks if the current color is not the same as task priority color
                        if (currentColor != colorCodes.get(m)) {
                            switch (currentColor) {
                                // for each current color of the day,
                                // check if the colorCode of the task is a higher priority
                                // and set text color to highest task priority color
                                case 0xFFFF0000: // red
                                    holder.calendarDay.setTextColor(currentColor);
                                    break;
                                case 0xFFFF8000: // orange
                                    if (colorCodes.get(m) == 0xFFFF0000) {
                                        holder.calendarDay.setTextColor(colorCodes.get(m));
                                    } else {
                                        holder.calendarDay.setTextColor(currentColor);
                                    }
                                    break;
                                case 0xFFFFFF00:  // yellow
                                    if (colorCodes.get(m) == 0xFFFF0000 || colorCodes.get(m) == 0xFFFF8000) {
                                        holder.calendarDay.setTextColor(colorCodes.get(m));
                                    } else {
                                        holder.calendarDay.setTextColor(currentColor);
                                    }
                                    break;
                                case 0xFFC5FF00:  // lime
                                    if (colorCodes.get(m) == 0xFFFF0000 || colorCodes.get(m) == 0xFFFFFF00 || colorCodes.get(m) == 0xFFFF8000) {
                                        holder.calendarDay.setTextColor(colorCodes.get(m));
                                    } else {
                                        holder.calendarDay.setTextColor(currentColor);
                                    }
                                    break;
                                case 0xFF00FF00:  // green
                                    if (colorCodes.get(m) == 0xFFFF0000 || colorCodes.get(m) == 0xFFFFFF00 || colorCodes.get(m) == 0xFFC5FF00 || colorCodes.get(m) == 0xFFFF8000) {
                                        holder.calendarDay.setTextColor(colorCodes.get(m));
                                    } else {
                                        holder.calendarDay.setTextColor(currentColor);
                                    }
                                    break;
                                default:
                                    holder.calendarDay.setTextColor(colorCodes.get(m));
                                    break;
                            }
                        }
                        currentColor = holder.calendarDay.getCurrentTextColor();
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
