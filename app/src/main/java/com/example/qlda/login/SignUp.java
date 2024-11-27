package com.example.qlda.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.text.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.Data.Data;
import com.example.qlda.Data.User;
import com.example.qlda.R;
import com.example.qlda.Utils.TimeUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.*;

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

        auth = FirebaseFirestore.getInstance();

        btnSignIn.setOnClickListener(v -> {
            Intent signIn = new Intent(SignUp.this, LoginActivity.class);
            startActivity(signIn);
            finish();
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
            User user = Data.getInstance().createUser(email, password, email.split("@")[0], 1, TimeUtils.getCurrentTimeFormatted());
            informSignUpSuccess(this, () ->{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            // Save user data to Firestore
//            auth.collection("users").document(user.getUserId())
//                    .set(user)
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(SignUp.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SignUp.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(SignUp.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        return;
//                    });
        });
    }
    private void informSignUpSuccess(Context context, Runnable actions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn đã đăng kí tài khoản thành công, hãy ấn OK để quay về đăng nhập");

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (actions != null) {
                actions.run();
            }
            dialog.dismiss();
        });

//        builder.setNegativeButton("Hủy", (dialog, which) -> {
//            dialog.dismiss();
//        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
