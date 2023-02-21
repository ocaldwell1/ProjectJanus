package com.example.janus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.janus.FriendRequest;
import com.example.janus.ItemClickListener;
import com.example.janus.R;
import com.example.janus.Task;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<com.example.janus.FriendRequestAdapter.MyViewHolder> {
    //Context context;
    ArrayList<FriendRequest> requestList;
    ItemClickListener clickListener;

    public FriendRequestAdapter(ArrayList<FriendRequest> requests) {
        this.requestList = requests;
    }

    @NonNull
    @Override
    public com.example.janus.FriendRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequestdisplayitem,parent, false);

        return new com.example.janus.FriendRequestAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.janus.FriendRequestAdapter.MyViewHolder holder, int position) {
        // add task
        //activity.user.setPosition(position);
        FriendRequest request = requestList.get(position);
        holder.sender.setText(request.getSender());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView sender;
        Button acceptButton;
        Button denyButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.sender = itemView.findViewById(R.id.friendRequestSenderText);
            this.acceptButton = itemView.findViewById(R.id.friendRequestAcceptButton);
            this.denyButton = itemView.findViewById(R.id.friendRequestDenyButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FriendRequestList friendRequestList = FriendRequestList.getInstance();
                    friendRequestList.accept(sender.getText().toString());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FriendRequestList friendRequestList = FriendRequestList.getInstance();
                    friendRequestList.deny(sender.getText().toString());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            //clickListener.onClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}