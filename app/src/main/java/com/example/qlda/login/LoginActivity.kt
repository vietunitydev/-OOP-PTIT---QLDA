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
import com.example.qlda.home.HomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRememberMe: CheckBox
    private lateinit var btnSignIn: Button
    private lateinit var tvNeedHelp: TextView

    // auth
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

        auth = Firebase.auth

        // Set click listener for Sign In button
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser

                            // If successful, we open home activity
                            Toast.makeText(
                                baseContext,
                                "Signed in as $email",
                                Toast.LENGTH_SHORT).show();

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()

                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
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
