package com.example.janus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {


    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        Button startFragmentStartButton = (Button) view.findViewById(R.id.startFragmentStartButton);
        startFragmentStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_startFragment_to_taskFragment);
            }
        });
        return view;
    }
}