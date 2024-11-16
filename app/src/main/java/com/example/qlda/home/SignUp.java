package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.text.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.Data.UserData;
import com.example.qlda.R;
import com.example.qlda.login.LoginActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.*;

import java.util.*;
public class SignUp extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etRePassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private TextView tvNeedHelp;

    private FirebaseFirestore auth;

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean checkPasswordLength(String password){
        return password != null && password.length() >= 8;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_sign_up);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        tvNeedHelp = (TextView) findViewById(R.id.tvNeedHelp );

        auth = FirebaseFirestore.getInstance();

        btnSignIn.setOnClickListener(v -> {
            Intent signIn = new Intent(SignUp.this, LoginActivity.class);
            startActivity(signIn);
            finish();
        });

        tvNeedHelp.setOnClickListener(v -> {
            // Example: Show a Toast message or navigate to a help screen
            Toast.makeText(this, "Help is on the way!", Toast.LENGTH_SHORT).show();
        });

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String rePassword = etRePassword.getText().toString().trim();

            if(!isValidEmail(email)){
                etEmail.setError("Invalid email address");
                etEmail.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is required");
                etPassword.requestFocus();
                return;
            }

            if(!checkPasswordLength(rePassword)){
                etPassword.setError("Password must be at least 8 characters");
                etPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(rePassword)) {
                etRePassword.setError("Please confirm your password");
                etRePassword.requestFocus();
                return;
            }


            if (!password.equals(rePassword)) {
                etRePassword.setError("Passwords do not match");
                etRePassword.requestFocus();
                return;
            }

            // Create User object
            UserData user = new UserData(UUID.randomUUID().toString(), email, password, email.split("@")[0], "", new Date(), new Date());

            // Save user data to Firestore
            auth.collection("users").document(user.getId())
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SignUp.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignUp.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    });
        });
    }
}
