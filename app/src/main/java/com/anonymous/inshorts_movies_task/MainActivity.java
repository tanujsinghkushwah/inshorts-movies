package com.anonymous.inshorts_movies_task;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String ARG_MOVIE_ID = "movieId";
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_bookmarks)
                .build();
        
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        handleDeepLink();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
    
    private void handleDeepLink() {
        if (getIntent() != null && getIntent().getData() != null) {
            String path = getIntent().getData().getPath();
            if (path != null && path.startsWith("/movie/")) {
                try {
                    int movieId = Integer.parseInt(path.replace("/movie/", ""));
                    Bundle args = new Bundle();
                    args.putInt(ARG_MOVIE_ID, movieId);
                    navController.navigate(R.id.navigation_movie_details, args);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    android.util.Log.e("MainActivity", "Invalid movie ID in deep link: " + path, e);
                }
            }
        }
    }
}