package com.example.qlda.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qlda.R;
import com.example.qlda.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private CheckBox cbRememberMe;
    private Button btnSignIn;
    private TextView tvNeedHelp;

    // Firebase authentication instance
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvNeedHelp = findViewById(R.id.tvNeedHelp);

        Log.d("Firebase", "1: Firebase Auth instance created");

        auth = FirebaseAuth.getInstance();

        Log.d("Firebase", "2: Firebase Auth instance initialized");

        // Set click listener for Sign In button
        btnSignIn.setOnClickListener(v -> {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Firebase", "3: Attempting to sign in with email: " + email);

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success
                    Log.d("Firebase", "4: Sign-in successful");
                    Toast.makeText(this, "Signed in as " + email, Toast.LENGTH_SHORT).show();
                    // Navigate to another activity
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Optionally close the current activity
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Firebase", "5: Sign-in failed", task.getException());
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

        // Set click listener for "Need help signing in?" text
        tvNeedHelp.setOnClickListener(v -> {
        // Example: Show a Toast message or navigate to a help screen
        Toast.makeText(this, "Help is on the way!", Toast.LENGTH_SHORT).show();
    });
    }
}
