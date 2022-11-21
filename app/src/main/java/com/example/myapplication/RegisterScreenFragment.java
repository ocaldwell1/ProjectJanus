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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.navigation.NavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button submitButton;
    private EditText firstName, lastName, password, email;
    private FirebaseAuth mAuth;
    private NavController navController;

    public RegisterScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterScreenFragment newInstance(String param1, String param2) {
        RegisterScreenFragment fragment = new RegisterScreenFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_screen, container, false);
        firstName = (EditText) view.findViewById(R.id.registerScreenFragFirstNameEditText);
        lastName = (EditText) view.findViewById(R.id.registerScreenFragLastNameEditText);
        email = (EditText) view.findViewById(R.id.registerScreenFragEmailEditText);
        password = (EditText) view.findViewById(R.id.registerScreenFragPasswordEditText);


        submitButton = (Button) view.findViewById(R.id.registerScreenFragSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerClick();
            }
        });
        /*
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMenu();
            }
        });*/
        // Inflate the layout for this fragment
        return view;
    }

    public void registerClick() {
        String userFirst = firstName.getText().toString().trim();
        String userLast = lastName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        String userID = mAuth.getCurrentUser().getUid();
        User regUser = new User(userFirst, userLast, userEmail, userID);

        if (userFirst.isEmpty()) {
            firstName.setError("First name required!");
            //return;
        }
        if (userLast.isEmpty()) {
            lastName.setError("Last name required!");
            //return;
        }
        if (userEmail.isEmpty()) {
            email.setError("Email required!");
            //return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Email is not valid!");
            email.requestFocus();
            //return;
        }
        if (userPass.isEmpty() || userPass.length() < 8) {
            password.setError("Password length needs to be at least 8 characters");
            password.requestFocus();
            // Add return statement here so all errors will be displayed
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // toast and then navigate to register complete fragment
                Toast.makeText(getActivity(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                // add user to fireStore function from User class
                regUser.addUserToFirestore(regUser);

                navController.navigate(R.id.regCompleteFragment);
            }
        });
    }
}