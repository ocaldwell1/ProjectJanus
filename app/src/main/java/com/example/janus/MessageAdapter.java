
package com.example.janus;
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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

        private ArrayList<Message> messages;
        private Context context;

    public MessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
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
            holder.textMessage.setText(messages.get(position).getContent());

            ConstraintLayout constraintLayout = holder.constraintLayout;
            if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.clear(R.id.profileCardview, ConstraintSet.LEFT);
                constraintSet.clear(R.id.messageContents, ConstraintSet.LEFT);
                constraintSet.connect(R.id.profileCardview, ConstraintSet.RIGHT, R.id.constraintLayout,
                        ConstraintSet.RIGHT, 0);
                constraintSet.connect(R.id.messageContents, ConstraintSet.RIGHT, R.id.profileCardview,
                        ConstraintSet.RIGHT, 0);
                constraintSet.applyTo(constraintLayout);
            } else {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.clear(R.id.profileCardview, ConstraintSet.RIGHT);
                constraintSet.clear(R.id.messageContents, ConstraintSet.RIGHT);
                constraintSet.connect(R.id.profileCardview, ConstraintSet.LEFT, R.id.constraintLayout,
                        ConstraintSet.LEFT, 0);
                constraintSet.connect(R.id.messageContents, ConstraintSet.LEFT, R.id.profileCardview,
                        ConstraintSet.RIGHT, 0);
                constraintSet.applyTo(constraintLayout);
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView textMessage;
        ImageView profileImage;
            public MessageHolder(@NonNull View itemView) {
                super(itemView);

                constraintLayout = itemView.findViewById(R.id.constraintLayout);
                textMessage = itemView.findViewById(R.id.messageContents);
                profileImage = itemView.findViewById(R.id.chatProfileImg);
            }
        }
    }


