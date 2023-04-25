package com.example.janus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.MyViewHolder> {
    //Context context;
    ArrayList<Task> taskList;
    ItemClickListener clickListener;

    public CompletedTaskAdapter(ArrayList<Task> task) {
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
        // add task
        //activity.user.setPosition(position);
        Task task = taskList.get(position);
        holder.name.setText(task.getName());
        holder.name.setTextColor(task.getPriorityColor());
        Log.i("COLORS", "got color " + task.getPriorityColor());

        /*
        try {
            holder.note.setText("" + task.getPriority()); //TODO change this back
        }
        catch(Exception e) {
            e.printStackTrace();
        }
         */
        holder.dueDate.setText(task.getDueDate());
        holder.source.setText(task.getSource());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, note, dueDate, source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.taskTitleName);
            //this.note = itemView.findViewById(R.id.taskNotes);
            this.dueDate = itemView.findViewById(R.id.taskDueDate);
            this.source = itemView.findViewById(R.id.taskSource);

            itemView.setOnClickListener(this); // bind the listener
        }

        @Override
        public void onClick(View view) {
            //clickListener.onClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
            //if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
