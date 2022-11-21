package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView login;
    private TextView forgotPass;
    private EditText logEmail, logPass;
    private Button backButton;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_screen, container, false);
        login = (TextView) view.findViewById(R.id.logScreenFragLogInTextView);
        forgotPass = (TextView) view.findViewById(R.id.logScreenFragForgotPassTextView);
        logEmail = (EditText) view.findViewById(R.id.logScreenFragEmailEditText);
        logPass = (EditText) view.findViewById(R.id.logScreenFragPasswordEditText);
        backButton = (Button) view.findViewById(R.id.logScreenFragBackButton);

        // Inflate the layout for this fragment
        return view;
    }

    public void loginNow() {
        String userEmail = logEmail.getText().toString().trim();
        String userPass = logPass.getText().toString().trim();

        if(userEmail.isEmpty()){
            logEmail.setError("Email required!");
            //return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            logEmail.setError("Email is not valid!");
            logEmail.requestFocus();
            //return;
        }
        if(userPass.isEmpty() || userPass.length() < 8){
            logPass.setError("Password length needs to be at least 8 characters");
            logPass.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Logged in!", Toast.LENGTH_SHORT).show();
                    // nav to log complete
                }else{
                    Toast.makeText(getActivity(), "Error! Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}