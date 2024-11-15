package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.R;


public class IssueActivity extends AppCompatActivity {
    private LayoutInflater inflater;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_dashboard);
        // Initialize LayoutInflater and LinearLayout
        inflater = LayoutInflater.from(this);
//        layout = findViewById(R.id.buttonContainer);

    }
}
