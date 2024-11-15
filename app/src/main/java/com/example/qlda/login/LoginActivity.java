package com.example.qlda.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qlda.R;
import com.example.qlda.home.HomeActivity;
import com.example.qlda.home.IssueDashBoard;
import com.example.qlda.home.LoginFragment;
import com.example.qlda.home.SignUp;
import com.example.qlda.home.WorkSpaceFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private CheckBox cbRememberMe;
    private Button btnSignIn;
    private Button btnSignUp;
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
        btnSignUp = findViewById(R.id.btnSignUp);
        tvNeedHelp = findViewById(R.id.tvNeedHelp);

        auth = FirebaseAuth.getInstance();

        // Retrieve stored user data
        UserData data = GetUserData();

        // Populate fields if rememberMe is true
        if (data.rememberMe) {
            etEmail.setText(data.username);
            etPassword.setText(data.password);
            cbRememberMe.setChecked(true);
        }

        // Set click listener for Sign In button
        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean rememberMe = cbRememberMe.isChecked();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("Firebase", "Attempting to sign in with email: " + email);

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Save user data if rememberMe is checked
                                if (rememberMe) {
                                    saveUserData(email, password, true);
                                } else {
                                    saveUserData("", "", false);
                                }
                                // Sign in success
                                Log.d("Firebase Login", "Sign-in successful - id " + Objects.requireNonNull(auth.getCurrentUser()).getUid());
                                Intent intent = new Intent(this, HomeActivity.class);
                                startActivity(intent);
                                finish(); // Optionally close the current activity
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase", "Sign-in failed", task.getException());
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnSignUp.setOnClickListener(v -> {
            Toast.makeText(this, "btnSignUp", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, IssueDashBoard.class);
            startActivity(intent);
            finish();
        });

        // Set click listener for "Need help signing in?" text
        tvNeedHelp.setOnClickListener(v -> {
            // Example: Show a Toast message or navigate to a help screen
            Toast.makeText(this, "Help is on the way!", Toast.LENGTH_SHORT).show();
        });
    }

    private UserData GetUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("username", null);
        String storedPassword = sharedPreferences.getString("password", null);
        boolean storedRememberMe = sharedPreferences.getBoolean("rememberMe", false);

        if (storedUsername == null && storedPassword == null && !storedRememberMe) {
            storedUsername = "";
            storedPassword = "";
            storedRememberMe = false;
        }

        return new UserData(storedUsername, storedPassword, storedRememberMe);
    }

    private void saveUserData(String username, String password, boolean rememberMe) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();
    }

    private static class UserData {
        String username;
        String password;
        boolean rememberMe;

        UserData(String username, String password, boolean rememberMe) {
            this.username = username;
            this.password = password;
            this.rememberMe = rememberMe;
        }
    }
}