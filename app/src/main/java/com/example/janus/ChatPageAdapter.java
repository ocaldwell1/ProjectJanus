
package com.example.janus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            holder.holder_username.setText(users.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ChatPageHolder extends RecyclerView.ViewHolder{
        TextView holder_username;
        ImageView holder_imageView;

        public ChatPageHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    onUserClickListener.OnUserClicked(getAdapterPosition());
                }
            });
            holder_username = itemView.findViewById(R.id.holder_username);
            holder_imageView = itemView.findViewById(R.id.holder_profile_img);
        }
    }
}


