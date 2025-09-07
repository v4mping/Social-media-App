package com.example.datingappandroidstudio.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.datingappandroidstudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button homeButton;
    Button profileButton;
    Button leaderboardButton;

    Button messagesButton;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            //No one is signed in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize nav buttons
        homeButton = findViewById(R.id.homeButton);
        profileButton = findViewById(R.id.profileButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);
        messagesButton = findViewById(R.id.messagesButton);

        // Attach listeners to nav buttons
        setupBottomNavListeners();

        // Check if user just registered
        boolean isNewUser = getIntent().getBooleanExtra("isNewUser", false);
        if (isNewUser) {
            loadFragment(new EditProfileFragment());
            return;
        }

        // Load main swiping screen by default
        if (savedInstanceState == null) {
            loadFragment(new MainFragment());
        }
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    //Method to navigate to specific fragment without making it the new main fragment.
    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    //Method to set up bottom navigation listeners.
    public void setupBottomNavListeners() {
        homeButton.setOnClickListener(v -> loadFragment(new MainFragment()));
        profileButton.setOnClickListener(v -> loadFragment(new Profile()));
        leaderboardButton.setOnClickListener(v -> loadFragment(new Leaderboard()));
        messagesButton.setOnClickListener(v -> loadFragment(new MessageFragment()));
    }

}
