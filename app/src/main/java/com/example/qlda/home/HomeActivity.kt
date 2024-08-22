package com.example.qlda.home

import android.os.Bundle
import com.example.qlda.R
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)
        // Initialize buttons
        val btnTable: Button = findViewById(R.id.btnTable)
        val btnCard: Button = findViewById(R.id.btnCard)
        val btnSearch: Button = findViewById(R.id.btnSearch)
        val btnNotification: Button = findViewById(R.id.btnNotification)
        val btnAccount: Button = findViewById(R.id.btnAccount)

        // Set click listeners
        btnTable.setOnClickListener {
            showToast("Table button clicked")
        }

        btnCard.setOnClickListener {
            showToast("Card button clicked")
        }

        btnSearch.setOnClickListener {
            showToast("Search button clicked")
        }

        btnNotification.setOnClickListener {
            showToast("Notification button clicked")
        }

        btnAccount.setOnClickListener {
            showToast("Account button clicked")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

