package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private User ouruser; //This can be renamed, potentially made public
    private FirebaseAuth mAuth;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private final int[] loginFragments = {
            R.id.logScreenFragment,
            R.id.registerScreenFragment,
            R.id.regCompleteFragment,
            R.id.forgotEmailFragment,
            R.id.forgotPasswordFragment
    };

    protected void onStart() {
        super.onStart();
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(contains(loginFragments, navDestination.getId())) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
                else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

        //if (user == null){
            //navController.navigate(R.id.loginFragment);
            //no, this should be in the start Fragment (UpcomingTaskFragment)
        //}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        User users = new User();
        //TODO Figure out the flow of loading user and task information and turn it into a method.
        //TODO call method from UpcomingTaskList (or do we need a start fragment?)
        //ouruser = new Users("ourfirst", "ourlast", "ouremail", "ourpass", 13);
        //TODO user = new User(some kind of Firebase access?)
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationUI.onNavDestinationSelected(item, navController);
                navController.popBackStack(item.getItemId(), false);
                return true;
            }
        });
    }

    private boolean contains(int[] array, int value) {
        for(int i = 0; i < array.length; i++){
            if(array[i] == value) {
                return true;
            }
        }
        return false;
    }
}