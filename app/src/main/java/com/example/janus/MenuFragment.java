package com.example.janus;

import static android.content.Intent.getIntent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class MenuFragment extends Fragment {
    private Button registerButton;
    private Button logButton;
    private FirebaseAuth mAuth;
    private NavController navController;
    private Uri uri;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getting the data from our intent in our uri.
        uri = getActivity().getIntent().getData();

        // checking if the uri is null or not.
        if (uri != null) {

            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            List<String> parameters = uri.getPathSegments();

            // after that we are extracting string
            // from that parameters.
            String param = parameters.get(parameters.size() - 1);

            // on below line we are setting that string
            // to our text view which we got as params.
            //messageTV.setText(param);
            Log.d("EX","Tagged");
        }

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Welcome");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        registerButton = (Button) view.findViewById(R.id.mainFragRegButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        logButton = (Button) view.findViewById(R.id.mainFragLogButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }

    public void openRegister() {
        // go to register screen
        navController.navigate(R.id.action_menuFragment_to_registerScreenFragment);

    }
    public void openLogin() {
        // go to log in screen
        navController.navigate(R.id.action_menuFragment_to_logScreenFragment);

    }
}
