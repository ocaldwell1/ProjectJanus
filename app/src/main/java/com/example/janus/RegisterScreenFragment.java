package com.example.janus;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.checkerframework.checker.nullness.qual.Nullable;

public class RegisterScreenFragment extends Fragment {

    private String userFirst;
    private String userLast;
    private String userEmail;
    private String userPass;
    private String userID;
    private Button submitButton;
    private EditText firstName, lastName, password, email;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private NavController navController;
    private ProgressBar progressBarRegScreen;

    public RegisterScreenFragment() {
        // Required empty public constructor
    }

    public static RegisterScreenFragment newInstance(String param1, String param2) {
        RegisterScreenFragment fragment = new RegisterScreenFragment();
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
        getActivity().setTitle("Register");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_screen, container, false);
    }

    @Override
    public void onViewCreated(@org.checkerframework.checker.nullness.qual.NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);

        firstName = (EditText) view.findViewById(R.id.registerScreenFragFirstNameEditText);
        lastName = (EditText) view.findViewById(R.id.registerScreenFragLastNameEditText);
        email = (EditText) view.findViewById(R.id.registerScreenFragEmailEditText);
        password = (EditText) view.findViewById(R.id.registerScreenFragPasswordEditText);
        progressBarRegScreen = view.findViewById(R.id.progressBarRegScreen);

        submitButton = (Button) view.findViewById(R.id.registerScreenFragSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerClick();
            }
        });
    }

    public void registerClick() {
        userFirst = firstName.getText().toString().trim();
        userLast = lastName.getText().toString().trim();
        userEmail = email.getText().toString().trim();
        userPass = password.getText().toString().trim();

        if (userFirst.isEmpty()) {
            firstName.setError("First name required!");
        }
        if (userLast.isEmpty()) {
            lastName.setError("Last name required!");
        }
        if (userEmail.isEmpty()) {
            email.setError("Email required!");
            // check @email.com pattern
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Email is not valid!");
            email.requestFocus();
        }
        if (userPass.isEmpty() || userPass.length() < 8) {
            password.setError("Password length needs to be at least 8 characters");
            password.requestFocus();
            // Add return statement here so all appropriate errors will be displayed
            return;
        }
        progressBarRegScreen.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // toast and then navigate to register complete fragment
                Toast.makeText(getActivity(), "Success! Logging you in!", Toast.LENGTH_SHORT).show();
                // add user to fireStore function from User class
                userID = mAuth.getCurrentUser().getUid();
                fUser = mAuth.getCurrentUser();
                //User regUser = new User(userFirst, userLast, userEmail, userID); commented out to move functionality to FireDataReader
                FireDataReader.getInstance().addUserToFireStore(userFirst, userLast, userEmail, userID);
                FireDataReader.getInstance().signIn(fUser);
                progressBarRegScreen.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "Logging you in!", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_registerScreenFragment_to_startFragment);
            } else {
                // for this else block to execute, the only error can be from inputting an existing email
                Toast.makeText(getActivity(), "Error! Email already registered.", Toast.LENGTH_SHORT).show();
                progressBarRegScreen.setVisibility(View.GONE);
            }
        });
    }
}