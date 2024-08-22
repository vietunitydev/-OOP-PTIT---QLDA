package com.example.qlda.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qlda.R
import com.example.qlda.home.HomeActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRememberMe: CheckBox
    private lateinit var btnSignIn: Button
    private lateinit var tvNeedHelp: TextView

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        cbRememberMe = findViewById(R.id.cbRememberMe)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvNeedHelp = findViewById(R.id.tvNeedHelp)

        Log.d("Firebase", "1: Firebase Auth instance created")

        auth = FirebaseAuth.getInstance()

        Log.d("Firebase", "2: Firebase Auth instance initialized")

        // Set click listener for Sign In button
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Firebase", "3: Attempting to sign in with email: $email")

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d("Firebase", "4: Sign-in successful")
                            val user = auth.currentUser
                            Toast.makeText(
                                baseContext,
                                "Signed in as $email",
                                Toast.LENGTH_SHORT).show();
                            // Navigate to another activity
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish() // Optionally close the current activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "5: Sign-in failed", task.exception)
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Set click listener for "Need help signing in?" text
        tvNeedHelp.setOnClickListener {
            // Example: Show a Toast message or navigate to a help screen
            Toast.makeText(this, "Help is on the way!", Toast.LENGTH_SHORT).show()
        }
    }
}
