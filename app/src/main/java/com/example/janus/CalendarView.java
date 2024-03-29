package com.example.janus;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarView extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView calendarDay;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarView(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        calendarDay = itemView.findViewById(R.id.calendarViewDay);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), (String) calendarDay.getText());
    }
}