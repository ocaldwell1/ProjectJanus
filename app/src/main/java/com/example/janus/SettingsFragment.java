package com.example.janus;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.janus.RegCompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NavController navController;
    private Button button;
    private TextView resetPassTV, logOutTV, changePicTV;

    public SettingsFragment() {
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
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Settings");
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);

        resetPassTV = view.findViewById(R.id.settingsResetPassTV);
        logOutTV = view.findViewById(R.id.settingsLogOutTV);
        changePicTV = view.findViewById(R.id.settingsChangePicTV);

        logOutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutAction();
            }
        });
        resetPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassAction();
            }
        });
        changePicTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicAction();
            }
        });
    }

    public void resetPassAction() {
        // navigate to reset pass fragment, where user will input info
        navController.navigate(R.id.action_settingsFragment_to_resetPasswordFragment);
    }

    public void logOutAction() {
        // execute log out
        //openDialog();
        // Check if user clicked yes then execute the following to log out:
        FireDataReader.getInstance().signOut();
        navController.navigate(R.id.action_settingsFragment_to_menuFragment);
        Toast.makeText(getActivity(), "Logged out!", Toast.LENGTH_SHORT).show();

    }
    public void changePicAction(){
        navController.navigate(R.id.action_settingsFragment_to_changeProfileFragment);
    }

    public void openDialog() {
        LogOutDialogFragment logOut = new LogOutDialogFragment();
        logOut.show(getActivity().getSupportFragmentManager(), "tag");
    }
}
