package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    //Context context;
    ArrayList<Task> taskList;
/**
    public TaskAdapter(UpcomingTasksFragment context, ArrayList<Task> task) {
        this.context = context;
        this.taskList = task;
    }**/
    public TaskAdapter( ArrayList<Task> task) {
      this.taskList=task;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskdisplayitem,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.name.setText(task.getTaskName());
        holder.note.setText(task.getTaskNote());
        holder.dueDate.setText(task.getTaskDueDate());
        holder.source.setText(task.getTaskSource());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, note, dueDate, source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.taskTitleName);
            this.note = itemView.findViewById(R.id.taskNotes);
            this.dueDate = itemView.findViewById(R.id.taskDueDate);
            this.source = itemView.findViewById(R.id.taskSource);
        }
    }
}
