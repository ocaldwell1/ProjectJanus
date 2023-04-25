
package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ChatPageAdapter extends RecyclerView.Adapter<ChatPageAdapter.ChatPageHolder> {
    private ArrayList<Contact> contacts;
    private ArrayList<String> imageURL;
    private Context context;
    private OnContactClickListener onContactClickListener;
    private Button deleteButton;
    int curr_pos;
    String imageUrl;
    ContactList contactList;
    public ChatPageAdapter(ArrayList<Contact> contacts, Context context, OnContactClickListener onContactClickListener) {
        this.contacts = contacts;
        this.context = context;
        this.imageURL = imageURL;
        this.onContactClickListener = onContactClickListener;
    }

    interface OnContactClickListener{
        void OnContactClicked(int position);
    }

    @Override
    public ChatPageHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_page_holder,parent,false);
        // set the image
        FireDataReader fireDataReader = FireDataReader.getInstance();
        //Log.d("GDGUY",String.valueOf(fireDataReader.getContactData(contacts.get(curr_pos).getEmail()).get("userImage")));
        imageUrl = String.valueOf(fireDataReader.getContactData(contacts.get(curr_pos).getEmail()).get("userImage"));
        return new ChatPageHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ChatPageHolder holder, int position) {
            holder.holder_username.setText(contacts.get(position).getEmail());
            curr_pos = position;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ChatPageHolder extends RecyclerView.ViewHolder{
        TextView holder_username;
       // ImageView holder_imageView;

        public ChatPageHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    onContactClickListener.OnContactClicked(getAdapterPosition());
                }
            });
            holder_username = itemView.findViewById(R.id.holder_username);
            //holder_imageView = itemView.findViewById(R.id.holder_profile_img);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            /**
            User user = User.getInstance();
            if(imageUrl.equals("default")){
            }else{
                Glide.with(context).load(imageUrl).into(holder_imageView);
            }
            **/
            // ask to delete the task
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create a dialog to delete the task
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete this friend chat forever?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            contactList = ContactList.getInstance();
                            contactList.remove(contacts.get(curr_pos));
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Navigation.findNavController(view).navigate(R.id.chatPageFragment);
                }
            });
        }
    }
}


