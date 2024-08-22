package com.example.qlda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.qlda.login.LoginActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optionally close the current activity
    }
}