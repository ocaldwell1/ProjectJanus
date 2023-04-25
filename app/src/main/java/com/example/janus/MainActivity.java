package com.example.janus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // creating a variable for our text view
    private TextView messageTV;
    private Uri uri;

    private FirebaseAuth mAuth;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private final int[] loginFragments = {
            R.id.logScreenFragment,
            R.id.registerScreenFragment,
            R.id.regCompleteFragment,
            R.id.forgotEmailFragment,
            R.id.forgotPasswordFragment,
            R.id.menuFragment,
            R.id.startFragment,
            //R.id.settingsFragment, // Will need bot nav
            R.id.changePicFragment,
            R.id.resetPassFragment
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
        //getMenuInflater().inflate(R.menu.profile_menu, menu);

        if(!User.isNotLoggedIn()) {
            // change profile pic to imageURL
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        // Here is the same process for bottomNavigationView selected
        if (item.getItemId() == R.id.settingsFragment && !User.isNotLoggedIn()) {
            NavigationUI.onNavDestinationSelected(item, navController);
            navController.popBackStack(item.getItemId(), false);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem settings = menu.findItem(R.id.settingsFragment);
        //MenuItem profile = menu.findItem(R.id.menu_item_profile);
        if(!User.isNotLoggedIn())
        {
            settings.setVisible(true);
            //profile.setVisible(true);
        }else
        {
            settings.setVisible(false);
            //profile.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting the data from our intent in our uri.
        uri = getIntent().getData();

        // checking if the uri is null or not.
        if (uri != null) {

            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            List<String> parameters = uri.getPathSegments();

            // after that we are extracting string
            // from that parameters.
            String param = parameters.get(parameters.size() - 1);

            // on below line we are setting that string
            // to our text view which we got as params.
            //messageTV.setText(param);
            Log.d("EX","Tagged");

            SharedPreferences sharedPreferences2=this.getSharedPreferences("save",MODE_PRIVATE);
            if(sharedPreferences2.getBoolean("value", false)){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationUI.onNavDestinationSelected(item, navController);
                navController.popBackStack(item.getItemId(), false);
                System.out.println("navigating to " + item.toString());
                return true;
            }
        });

        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        boolean themeState = getSharedPreferences("save", MODE_PRIVATE).getBoolean("value", false);
        if(themeState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        System.out.println(getIntent().toString());
    }

    private boolean contains(int[] array, int value) {
        for(int i = 0; i < array.length; i++){
            if(array[i] == value) {
                return true;
            }
        }
        return false;
    }
    /*@Override
    public void recreate(){
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getIntent());
    }*/

}