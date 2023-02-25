package com.example.janus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ChangePicFragment extends Fragment {
    private NavController navController;
    private Button saveButton;
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
        saveButton = view.findViewById(R.id.changePicSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePictureAction();
            }
        });

    }

    public void changePictureAction(){
        // execute picture change here

        // toast success
        Toast.makeText(getActivity(), "Profile picture saved!", Toast.LENGTH_SHORT).show();
    }
}
