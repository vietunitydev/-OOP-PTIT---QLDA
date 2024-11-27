package com.example.qlda.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.Data.Data;
import com.example.qlda.Data.User;
import com.example.qlda.R;
import com.example.qlda.home.HomeActivity;
import com.example.qlda.home.MyCustomLog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
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

                User loginUser = AuthLogin(email,password);
                if(loginUser != null) {
                    if (rememberMe) {
                        saveUserData(email, password, true);
                    } else {
                        saveUserData("", "", false);
                    }
                    Data.currentUser = loginUser;
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Data.currentUser = null;
                    // If sign in fails, display a message to the user.
                    MyCustomLog.DebugLog("Firebase", "Sign-in failed");
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
            finish();
        });

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

    private User AuthLogin(String email, String pass){
        Data data = Data.getInstance();
        List<User> users = data.getAllUsers();
        for(User user : users){
            if(Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), pass)){
                return user;
            }
        }

        return null;
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