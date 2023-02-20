package com.example.janus;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarDayTaskAdapter extends RecyclerView.Adapter<CalendarDayTaskAdapter.MyViewHolder> {
    //Context context;
    ArrayList<Task> taskList;
    ItemClickListener clickListener;
    String dueDate;
    /**
     public TaskAdapter(UpcomingTasksFragment context, ArrayList<Task> task) {
     this.context = context;
     this.taskList = task;
     }**/
    public CalendarDayTaskAdapter(ArrayList<Task> task, String dueDate) {
        this.taskList=task;
        this.dueDate=dueDate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_task_display,parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        Log.d(TAG, "thisDueDate: " + this.dueDate);
        Log.d(TAG, "getTaskDue: " + task.getDueDate());
        if(eventDate(task.getDueDate(),this.dueDate).equals(this.dueDate)){
            holder.name.setText(task.getName());
            holder.name.setTextColor(task.getPriorityColor());
        }
    }

    // converting numerical date to word date, (from 01/01/2023 to Jan 1, 2023)
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
    public int getItemCount() {
        return taskList.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.taskTitleName);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            //clickListener.onClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
