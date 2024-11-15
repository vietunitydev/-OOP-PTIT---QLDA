package com.example.qlda.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qlda.R;

public class AppNavigation {

    private final Context context;
    private final LinearLayout contentButton;
    private final FrameLayout content;

    private FrameLayout viewContainer;
    private View projectView;
    private View notificationView;
    private View issueView;


    public AppNavigation(Context context, LinearLayout layout, FrameLayout fragment) {
        this.context = context;
        this.contentButton = layout;
        this.content = fragment;
        initializeButtons();
    }

    public void initializeButtons() {
        Button btnProject = contentButton.findViewById(R.id.btn_Projects);
        Button btnNotification = contentButton.findViewById(R.id.btnNotification);
        Button btnIssue = contentButton.findViewById(R.id.btn_Issue);

        LayoutInflater inflater = LayoutInflater.from(context);
        projectView = inflater.inflate(R.layout.activity_project, null);
        notificationView = inflater.inflate(R.layout.screen_user, null);
        issueView = inflater.inflate(R.layout.issue_dashboard, null);

        // Gắn viewContainer
        viewContainer = content.findViewById(R.id.viewContainer);

        setActiveView(projectView);

        btnProject.setOnClickListener(v -> setActiveView(projectView));
        btnNotification.setOnClickListener(v -> setActiveView(notificationView));
        btnIssue.setOnClickListener(v -> setActiveView(issueView));
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void setActiveView(View view) {
        // Xóa tất cả các View cũ
        viewContainer.removeAllViews();

        // Thêm View mới vào container
        viewContainer.addView(view);
    }
}
