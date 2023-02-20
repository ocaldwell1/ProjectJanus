
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
    private ArrayList<Contact> contacts;
    private Context context;
    private OnContactClickListener onContactClickListener;
    public ChatPageAdapter(ArrayList<Contact> contacts, Context context, OnContactClickListener onContactClickListener) {
        this.contacts = contacts;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
    }

    interface OnContactClickListener{
        void OnContactClicked(int position);
    }

    @Override
    public ChatPageHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_page_holder,parent,false);
        return new ChatPageHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ChatPageHolder holder, int position) {
            holder.holder_username.setText(contacts.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ChatPageHolder extends RecyclerView.ViewHolder{
        TextView holder_username;
        ImageView holder_imageView;

        public ChatPageHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    onContactClickListener.OnContactClicked(getAdapterPosition());
                }
            });
            holder_username = itemView.findViewById(R.id.holder_username);
            holder_imageView = itemView.findViewById(R.id.holder_profile_img);
        }
    }
}


