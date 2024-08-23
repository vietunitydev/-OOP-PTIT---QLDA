package com.example.qlda.home

import CustomButtonWorkspace
import android.os.Bundle
import android.view.LayoutInflater
import com.example.qlda.R
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

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

        val buttonsData = listOf(
            Triple(1, "CTDL", ContextCompat.getColor(this, R.color.blue)),
            Triple(2, "English", ContextCompat.getColor(this, R.color.purple)),
            Triple(3, "Meta rush", ContextCompat.getColor(this, R.color.pink)),
            Triple(4, "Study", ContextCompat.getColor(this, R.color.deep_purple))
        )

        //TODO: need map to manage the id of specific button
        val layout = findViewById<LinearLayout>(R.id.buttonContainer)
        val inflater = LayoutInflater.from(this)

        buttonsData.forEach { (id,text, color) ->
//            val button = CustomButtonWorkspace(this)
//            button.setButtonProperties(id,text, color)

            val button = inflater.inflate(R.layout.custom_button_table_workspace, layout, false) as AppCompatButton
            button.id = id
            button.text = text
            button.setBackgroundColor(color)

            val toastMessage = String.format("Click Button ID: %d, Text: %s",id,text)
            button.setOnClickListener{
                (showToast(toastMessage))
            }

            layout.addView(button)
        }
        var i:Int = 5
        val addBtn = findViewById<Button>(R.id.btnAdd)
        val toastMessage = String.format("Click Add Table Button")
        addBtn.setOnClickListener {
            (showToast(toastMessage))
            val button = inflater.inflate(R.layout.custom_button_table_workspace, layout, false) as AppCompatButton
            button.id = i
            button.text = "New Table"
            button.setBackgroundColor(getRandomColor())

            val toastMessage = String.format("Click Button ID: %d, Text: %s",button.id,button.text)
            button.setOnClickListener{
                (showToast(toastMessage))
            }

            layout.addView(button)
            i++
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getRandomColor(): Int {
        val colors = listOf(
            ContextCompat.getColor(this, R.color.blue),
            ContextCompat.getColor(this, R.color.purple),
            ContextCompat.getColor(this, R.color.pink),
            ContextCompat.getColor(this, R.color.deep_purple)
        )

        return colors.random()
    }
}



