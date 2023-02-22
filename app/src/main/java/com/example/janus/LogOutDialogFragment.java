package com.example.janus;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.janus.RegCompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogOutDialogFragment extends AppCompatDialogFragment {

    private NavController navController;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //mess = getArguments().getString("setting");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    // user chooses cancel, do nothing
                    case DialogInterface.BUTTON_NEGATIVE:
                        // do nothing when Cancel is pressed.
                        Log.d(TAG, "logOut pressed: Cancel");
                        break;

                    case DialogInterface.BUTTON_POSITIVE:
                        // Execute log out action when Yes is pressed. Not working
                        logOutAction();
                        Log.d(TAG, "logOut pressed: Yes");
                        break;
                }
            }
        };
        builder.setTitle("Are you sure you want to log out?")
                .setMessage("Press Yes to continue.")
                .setNegativeButton("Cancel", listener)
                .setPositiveButton("Yes", listener);
        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
    }

    public void logOutAction(){
        FireDataReader.getInstance().signOut();
        navController.navigate(R.id.action_settingsFragment_to_menuFragment);
        Log.d(TAG, "logOut: Success");
    }
}
