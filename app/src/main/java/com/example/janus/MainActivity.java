package com.example.janus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
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
    }
// for settings button navigation
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // inflates the menu item onto action bar
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        // Here is the same process for bottomNavigationView selected
        if (item.getItemId() == R.id.settingsFragment) {
            NavigationUI.onNavDestinationSelected(item, navController);
            navController.popBackStack(item.getItemId(), false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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