package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ChatPageAdapter extends RecyclerView.Adapter<ChatPageAdapter.ChatPageHolder> {
    private ArrayList<User> users;
    private Context context;
    private OnUserClickListener onUserClickListener;
    public ChatPageAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    interface OnUserClickListener{
        void OnUserClicked(int position);
    }

    @Override
    public ChatPageHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_page_holder,parent,false);
        return new ChatPageHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ChatPageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ChatPageHolder extends RecyclerView.ViewHolder{
        public ChatPageHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
