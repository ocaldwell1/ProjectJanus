package com.example.janus;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.Nullable;

public class logScreenFragment extends Fragment {
    private TextView login;
    private TextView forgotPass;
    private EditText logEmail, logPass;
    private NavController navController;

    private FirebaseAuth mAuth;

    public logScreenFragment() {
        // Required empty public constructor
    }

    public static logScreenFragment newInstance(String param1, String param2) {
        logScreenFragment fragment = new logScreenFragment();
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
        getActivity().setTitle("Log in");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_screen, container, false);
    }

    @Override
    public void onViewCreated(@org.checkerframework.checker.nullness.qual.NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        login = (TextView) view.findViewById(R.id.logScreenFragLogInTextView);
        forgotPass = (TextView) view.findViewById(R.id.logScreenFragForgotPassTextView);
        logEmail = (EditText) view.findViewById(R.id.logScreenFragEmailEditText);
        logPass = (EditText) view.findViewById(R.id.logScreenFragPasswordEditText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginNow();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

    }

    public String getLogInMessage(String message){
        String msg;
        if (message.equals("Logged In!")){
            msg = "Success";
        }else {
            msg = "Failed";
        }
        return msg;
    }

    public void loginNow() {
        String userEmail = logEmail.getText().toString().trim();
        String userPass = logPass.getText().toString().trim();

        if(userEmail.isEmpty()){
            logEmail.setError("Email required!");
            logEmail.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            logEmail.setError("Email is not valid!");
            logEmail.requestFocus();
        }
        if(userPass.isEmpty() || userPass.length() < 8){
            logPass.setError("Password length needs to be at least 8 characters");
            logPass.requestFocus();
        }
        FireDataReader fireDataReader = FireDataReader.getInstance();
        boolean success = fireDataReader.signIn(userEmail, userPass);
        Log.d(TAG, "Logged in boolean: " + success);
        // This is not synced correctly. It will not be successful on the first log in attempt
        // The second attempt will work
        if(success) {
        //if(!User.isNotLoggedIn()){
            String message = "Logged in!";
            getLogInMessage(message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            // first load tasks, to prevent issues
            TaskList.getInstance().getTaskList();

            // nav to log complete / upcoming assignments
            navController.navigate(R.id.action_logScreenFragment_to_taskFragment);
        }
        else {
            String message = "Error! Invalid Credentials!";
            getLogInMessage(message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotPassword() {
        navController.navigate(R.id.action_logScreenFragment_to_forgotPasswordFragment);
    }


}