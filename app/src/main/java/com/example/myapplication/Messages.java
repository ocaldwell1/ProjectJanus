package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class Messages extends RecyclerView.Adapter<Messages.MessageHolder> {
     private ArrayList<Message> messages;
     private String senderImg, receiverImg;
     private Context context;

    public Messages(ArrayList<Message> messages, String senderImg, String receiverImg, Context context) {
        this.messages = messages;
        // sender and receiver img is for showing receiver and sender past messages
        this.senderImg = senderImg;
        this.receiverImg = receiverImg;
        this.context = context;
    }

    @NonNull
     @Override
     public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.message_holder, parent, false);
         return new MessageHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
         holder.message.setText(messages.get(position).getContent());

         ConstraintLayout constraintLayout = holder.constraintLayout;
         // TODO add firebase auth. to check if sender
         //if(messages.get(position).getSender().equals())

         //Moves sender message/profile img to correct side of phone screen (right) at runtime

         ConstraintSet constraintSet = new ConstraintSet();
         constraintSet.clone(constraintLayout);
         constraintSet.clear(R.id.CardView, ConstraintSet.LEFT);
         constraintSet.clear(R.id.messageContents, ConstraintSet.LEFT);
         constraintSet.connect(R.id.CardView, constraintSet.RIGHT, R.id.constraintLayout, constraintSet.RIGHT, 0);
         constraintSet.connect(R.id.messageContents, constraintSet.RIGHT, R.id.constraintLayout, constraintSet.RIGHT, 0);
         constraintSet.applyTo(constraintLayout);
         //person is receiver, aligns messages to left
         /*else{
             ConstraintSet constraintSet = new ConstraintSet();
             constraintSet.clone(constraintLayout);
             constraintSet.clear(R.id.CardView, ConstraintSet.RIGHT);
             constraintSet.clear(R.id.messageContents, ConstraintSet.RIGHT);
             constraintSet.connect(R.id.CardView, constraintSet.LEFT, R.id.constraintLayout, constraintSet.LEFT, 0);
             constraintSet.connect(R.id.messageContents, constraintSet.LEFT, R.id.constraintLayout, constraintSet.RIGHT, 0);
             constraintSet.applyTo(constraintLayout);
     }*/
     }

     @Override
     public int getItemCount() {
         return messages.size();
     }

     class MessageHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView message;
        ImageView profileImage;

         public MessageHolder(@NonNull View itemView) {
             super(itemView);
             constraintLayout = itemView.findViewById(R.id.constraintLayout);
             message = itemView.findViewById(R.id.messageContents);
             profileImage = itemView.findViewById(R.id.chatProfileImg);

         }
     }
 }
