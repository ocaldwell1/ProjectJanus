package com.example.janus;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;

public class ChangePicFragment extends Fragment {
    private NavController navController;
    private Button saveButton, editButton;
    private ImageView picture;
    DatabaseReference reference;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    public ChangePicFragment() {
        // Required empty public constructor
    }

    public static ChangePicFragment newInstance(String param1, String param2) {
        ChangePicFragment fragment = new ChangePicFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Change profile picture");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        picture = view.findViewById(R.id.changePicImageView);
        editButton = view.findViewById(R.id.changePicEditButton);
        saveButton = view.findViewById(R.id.changePicSaveButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPictureAction();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePictureAction();
            }
        });

        User user = User.getInstance();
        if(user.getImageURL().equals("null")){

        }else{
            Glide.with(getActivity()).load(user.getImageURL()).into(picture);
        }

    }
    // edit picture
    public void editPictureAction(){
        //open gallery
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        openGallery.setType("image/*");
        // open activity wizard
        startActivityForResult(openGallery, 100);
    }


    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 & resultCode == RESULT_OK){
            Uri image = data.getData();

            String filepath = "Photos/" + "user_" + fUser.getUid();
            // adds image to firebase storage with the file path
            StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            User user = User.getInstance();
                            user.setImageURL(imageURL);
                            // below line will load selected image into the drawable
                            Glide.with(getActivity()).load(imageURL).into(picture);
                        }
                    });
                }
            });
        }
    }

    // navigate back to settings
    public void savePictureAction(){
        // execute picture change here

        // toast success
        Toast.makeText(getActivity(), "Profile picture saved!", Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.action_changePicFragment_to_settingsFragment);
    }

}
