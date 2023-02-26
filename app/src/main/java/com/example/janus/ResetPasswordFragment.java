package com.example.janus;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ResetPasswordFragment extends Fragment {
    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link com.example.janus.RegCompleteFragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NavController navController;
    private Button submitButton;
    private EditText oldPass, newPass, email;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegCompleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireDataReader.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Password Reset");
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_reset_password, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        email = view.findViewById(R.id.editTextEmail);
        oldPass = view.findViewById(R.id.editTextOldPassword);
        newPass = view.findViewById(R.id.editTextNewPassword);
        submitButton = view.findViewById(R.id.resetPassSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userOldPass = oldPass.getText().toString();
                String userNewPass = newPass.getText().toString();
                resetPassAction(userEmail, userOldPass, userNewPass);
            }
        });

    }

    // reset password function, will execute reset function in FireDataReader class
    public void resetPassAction(String uEmail, String oldP, String newP) {
        // Having trouble grabbing user Data.
        Log.d(TAG, "getUserData: " + FireDataReader.getInstance().getUserData());
        // if there is no change in passwords
        Log.d(TAG, "input email: " + uEmail);
        Log.d(TAG, "input old: " + oldP);
        Log.d(TAG, "input new: " + newP);
        // checks if user is logged in
        if(!User.isNotLoggedIn()) {
            User user = User.getInstance();
            FireDataReader fireDataReader = FireDataReader.getInstance();
            // ERROR: user.getEmail() is null when I log in and then try to reset password
            if (!(uEmail.equals(user.getEmail())) || uEmail.equals("")) {
                //Toast.makeText(getActivity(), "Error! Enter your current email.", Toast.LENGTH_LONG).show();
                email.setError("Error! Enter your current email.");
                email.requestFocus();
                // check if email pattern
            } else if (oldP.length() < 8 || oldP.equals("")) {
                //Toast.makeText(getActivity(), "Error! Passwords need to be at least 8 characters.", Toast.LENGTH_LONG).show();
                oldPass.setError("Error! Passwords need to be at least 8 characters.");
                oldPass.requestFocus();
            } else if (newP.equals("") || newP.length() < 8) {
                newPass.setError("Error! Passwords need to be at least 8 characters.");
                newPass.requestFocus();
            } else if (oldP.equals(newP)) {
                //Toast.makeText(getActivity(), "Error! Password is the same as before.", Toast.LENGTH_LONG).show();
                newPass.setError("Error! Password is the same as before.");
                newPass.requestFocus();
            } else {
                Log.d(TAG, "Password reset here!");
                fireDataReader.resetPassword(uEmail, oldP, newP);

                Toast.makeText(getActivity(), "Password has been reset!", Toast.LENGTH_SHORT).show();
                // Navigate back to settings after resetting.
                // Right now this will navigate regardless of a successful password reset
                // Need to implement check successful reset and then navigate
                navController.navigate(R.id.action_resetPasswordFragment_to_settingsFragment);
            }
        }
    }

}