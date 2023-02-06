package com.example.janus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link logScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class logScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView login;
    private TextView forgotPass;
    private EditText logEmail, logPass;
    private NavController navController;

    private FirebaseAuth mAuth;

    public logScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment logScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static logScreenFragment newInstance(String param1, String param2) {
        logScreenFragment fragment = new logScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        if(success) {
            String message = "Logged in!";
            getLogInMessage(message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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