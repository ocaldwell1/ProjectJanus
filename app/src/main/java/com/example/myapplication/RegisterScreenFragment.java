package com.example.myapplication;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

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

    private String userFirst;
    private String userLast;
    private String userEmail;
    private String userPass;
    private String userID;
    private Button submitButton;
    private EditText firstName, lastName, password, email;
    private FirebaseAuth mAuth;
    private DocumentReference documentReference;
    private FirebaseFirestore db;
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
        db = FirebaseFirestore.getInstance();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // toast and then navigate to register complete fragment
                Toast.makeText(getActivity(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                // add user to fireStore function from User class
                userID = mAuth.getCurrentUser().getUid();
                User regUser = new User(userFirst, userLast, userEmail, userID);
                regUser.addUserToFireStore(userFirst, userLast, userEmail, userID);
               /* documentReference = db.collection("User").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("userFirstName", userFirst);
                user.put("userLastName", userLast);
                user.put("userEmail", userEmail);
                user.put("userID", userID);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Success: Added to FireStore");
                    }
                });*/
                //mAuth.signOut();
                navController.navigate(R.id.action_registerScreenFragment_to_regCompleteFragment);
            } else {
                Toast.makeText(getActivity(), "Error! Cannot register!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}