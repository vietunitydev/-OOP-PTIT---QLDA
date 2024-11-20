package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.R;

public class DetailIssue extends AppCompatActivity {
    private ImageButton imgBtnAddIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.issue_details);

        imgBtnAddIssues = (ImageButton) findViewById(R.id.imgBtnAddIssues);

        imgBtnAddIssues.setOnClickListener(v ->{
            finish();
        });
    }
}
