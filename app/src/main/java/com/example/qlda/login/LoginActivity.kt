package com.example.qlda.login

import android.content.Intent
import com.example.qlda.R;
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRememberMe: CheckBox
    private lateinit var btnSignIn: Button
    private lateinit var tvNeedHelp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        cbRememberMe = findViewById(R.id.cbRememberMe)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvNeedHelp = findViewById(R.id.tvNeedHelp)

        // Set click listener for Sign In button
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                // Perform sign-in logic here (e.g., API call)
                // Example: Show a simple Toast message
                Toast.makeText(this, "Signed in as $email", Toast.LENGTH_SHORT).show()

                // If successful, you can navigate to another activity
                // Example:
                // val intent = Intent(this, HomeActivity::class.java)
                // startActivity(intent)
                // finish()
            }
        }

        // Set click listener for "Need help signing in?" text
        tvNeedHelp.setOnClickListener {
            // Example: Show a Toast message or navigate to a help screen
            Toast.makeText(this, "Help is on the way!", Toast.LENGTH_SHORT).show()
        }
    }
}
