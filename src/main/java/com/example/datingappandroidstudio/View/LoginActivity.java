package com.example.datingappandroidstudio.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datingappandroidstudio.Controller.Controller;
import com.example.datingappandroidstudio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginActivity#} factory    method to
 * create an instance of this fragment.
 */
public class LoginActivity extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText emailText, passwordText;
    private Button logInButton, registerButton;

    public LoginActivity() {
        // Required empty public constructor
    }
/*
This method is on the onCreate with the added functionality of making buttons work and stuff.
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_log_in);

        controller = Controller.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        logInButton = findViewById(R.id.LogInButton);
        registerButton = findViewById(R.id.registerButton);

        logInButton.setOnClickListener(v -> attemptLogin());
        registerButton.setOnClickListener(v -> attemptRegister());
    }
/*
I made attemptLogin and attemptRegister methods to make sure that we can catch errors and load
things using the controller without the whole app crashing.
 */
    private void attemptLogin() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // 1) At this point auth is good; now load the full profile
                    controller.loadUserProfileFromFirestore(
                            user -> {
                                // 2) Firestore doc has been deserialized into your model
                                // Now start up MainActivity
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            },
                            error -> {
                                Toast.makeText(this,
                                        "Failed to load profile: " + error.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                    );
                });

    }

    private void attemptRegister() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Make sure to input both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        db.collection("users").document(uid).set(userData);
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("isNewUser", true);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}