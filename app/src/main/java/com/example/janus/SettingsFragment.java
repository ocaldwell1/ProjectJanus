package com.example.janus;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.UiModeManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.compose.runtime.Composable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Modifier;

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
    private Spinner priorityCalcSpinner;
    private TextView resetPassTV, logOutTV, changePicTV, firstName, lastName, uEmail;
    private ImageView picture;
    private SwitchMaterial darkThemeSwitch;
    SharedPreferences sharedPreferences;

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

        firstName = view.findViewById(R.id.settingsFirstNameTV);
        lastName = view.findViewById(R.id.settingsLastNameTV);
        uEmail = view.findViewById(R.id.settingsEmailTV);
        firstName.setText(User.getInstance().getFirstName());
        lastName.setText(User.getInstance().getLastName());
        uEmail.setText(User.getInstance().getEmail());
        picture = view.findViewById(R.id.settingsImageView);

        priorityCalcSpinner = view.findViewById(R.id.settingsPriorityCalculationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.priorityStrategies, android.R.layout.simple_spinner_item);
        priorityCalcSpinner.setAdapter(adapter);
        priorityCalcSpinner.setSelection(User.getInstance().getPriorityMethodIndex());
        resetPassTV = view.findViewById(R.id.settingsResetPassTV);
        logOutTV = view.findViewById(R.id.settingsLogOutTV);
        changePicTV = view.findViewById(R.id.settingsChangePicTV);
        darkThemeSwitch = view.findViewById(R.id.darkThemeSwitch);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("save",MODE_PRIVATE);
        darkThemeSwitch.setChecked(sharedPreferences.getBoolean("value",true));
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    darkThemeSwitch.setChecked(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    darkThemeSwitch.setChecked(false);
                }
            }
            });



        priorityCalcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();
                User.getInstance().setPriorityCalculator(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

        User user = User.getInstance();
        if(user.getImageURL().equals("default")){

        }else{
            Glide.with(getActivity()).load(user.getImageURL()).into(picture);
        }
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
        User.getInstance().signOut();
        TaskList.getInstance().signOut();
        ContactList.getInstance().signOut();
        FriendRequestList.getInstance().signOut();
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
