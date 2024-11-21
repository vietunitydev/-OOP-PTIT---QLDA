package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.R;

public class DetailIssue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.issue_details);


        rollBackIssue();
        assignDataIssue();
    }
    private void rollBackIssue(){
        ImageButton imgBtnBackIssues;
        imgBtnBackIssues = (ImageButton) findViewById(R.id.imgBtnBackIssues);

        imgBtnBackIssues.setOnClickListener(v ->{
            finish();
        });
    }
    private void assignDataIssue(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int taskId = bundle.getInt("taskId");
            String taskName = bundle.getString("taskName");
            String description = bundle.getString("description");
            int assignedTo = bundle.getInt("assignedTo");
            int projectId = bundle.getInt("projectId");
            String priority = bundle.getString("priority");
            String status = bundle.getString("status");
            String dueDate = bundle.getString("dueDate");

            //Gán dữ liêuj
            EditText edtNameIssue = (EditText) findViewById(R.id.edtNameIssue);
            edtNameIssue.setText(taskName);

            TextView txtStatusIssue = (TextView) findViewById(R.id.txtStatusIssue);
            txtStatusIssue.setText(status);
        }
    }
}
