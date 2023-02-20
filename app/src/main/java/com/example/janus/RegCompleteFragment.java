package com.example.janus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class RegCompleteFragment extends Fragment {

    private Button menuButton;
    private FirebaseAuth mAuth;
    private NavController navController;

    public RegCompleteFragment() {
        // Required empty public constructor
    }

    public static RegCompleteFragment newInstance(String param1, String param2) {
        RegCompleteFragment fragment = new RegCompleteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Register Complete");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reg_complete, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);

        menuButton =  (Button) view.findViewById(R.id.regCompleteFragMenuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });

    }

    public void goToMenu(){
        // sign out user
        mAuth.signOut();
        navController.navigate(R.id.action_regCompleteFragment_to_menuFragment);
    }

}