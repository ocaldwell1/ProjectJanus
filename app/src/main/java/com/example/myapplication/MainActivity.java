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

public class MainActivity extends AppCompatActivity {
    public User user; //This can be renamed, potentially made public
    private FirebaseAuth mAuth;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private final int[] loginFragments = {
            R.id.logScreenFragment,
            R.id.registerScreenFragment,
            R.id.regCompleteFragment,
            R.id.forgotEmailFragment,
            R.id.forgotPasswordFragment,
            R.id.menuFragment,
            R.id.startFragment
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

        if (user == null){
            //navController.navigate(R.id.logScreenFragment);
            //no, this should be in the start Fragment (UpcomingTaskFragment)
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User();
        if (user != null) {
            Log.i("MAIN", "" + user.isLoggedIn());
        }
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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