package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.R;
import com.example.qlda.login.LoginActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class IssueDashBoard extends AppCompatActivity {
    private ImageButton imgBtnSearch;
    private ImageButton imgBtnAddIssues;
    private EditText edtSearch;

    private FirebaseFirestore auth;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_dashboard);

        imgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);
        imgBtnAddIssues = (ImageButton) findViewById(R.id.imgBtnAddIssues);
        edtSearch = (EditText) findViewById(R.id.edtSearch);

        auth = FirebaseFirestore.getInstance();

        imgBtnSearch.setOnClickListener(v -> {
            if(edtSearch.getVisibility() == View.GONE){
                edtSearch.setVisibility(View.VISIBLE);
            }
            else{
                edtSearch.setVisibility(View.GONE);
            }
        });
    }
}