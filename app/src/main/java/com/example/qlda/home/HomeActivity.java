package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.qlda.Data.Data;
import com.example.qlda.Data.Parser;
import com.example.qlda.Data.Priority;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.StatusType;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.TaskType;
import com.example.qlda.Data.User;
import com.example.qlda.Data.TimeGroup;
import com.example.qlda.R;
import com.example.qlda.login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout viewContainer;

    // 3 view will use
    View projectView;
    User user;

    private BottomSheetDialog curBottomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        viewContainer = findViewById(R.id.viewContainer);

        user = Data.currentUser;

        initializeButtons();
        fetchUserProject();
        setupCreateProject();
        setupShowUserInfo();
        setupSearchProjectByName();

    }

    // add function show for 3 button
    public void initializeButtons() {
        Button btnIssue = findViewById(R.id.btn_Issue);
        Button btnNotification = findViewById(R.id.btnNotification);

        projectView = getLayoutInflater().inflate(R.layout.activity_project, null);
        viewContainer.removeAllViews();
        viewContainer.addView(projectView);

        btnNotification.setOnClickListener(v -> {

        });
        btnIssue.setOnClickListener(v ->{
            Intent intent = new Intent(this, IssueActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //=========================================================================

    private void fetchUserProject(){
        Data data = Data.getInstance();
        for (Project project : data.getProjectsByUserId(user.getUserId())) {
            createButton(project);
        }

    }

    private void setupCreateProject(){
        Button addProject = projectView.findViewById(R.id.wl_content_btnAdd);
        // Add new button on click
        addProject.setOnClickListener(v -> {

            View view = showBottomSheetDialog(R.layout.create_project);

            EditText projectNameInput = view.findViewById(R.id.project_name_input);
            EditText projectKeyInput = view.findViewById(R.id.project_key_input);
            Button createButton = view.findViewById(R.id.create_button);

            createButton.setOnClickListener(e -> {

                String projectName = projectNameInput.getText().toString().trim();
                String projectKey = projectKeyInput.getText().toString().trim();

                if(projectName.equals("") || projectKey.equals("")){
                    return;
                }

//                FireStoreHelper fs = new FireStoreHelper();
                MyCustomLog.Toast(this,"Click Add Table Button");

                Project newProject = Data.getInstance().createProject(projectName,"","","","",2);
                createButton(newProject);

                curBottomDialog.dismiss();
            });

        });
    }
    private void createButton(Project project) {
        LinearLayout content = projectView.findViewById(R.id.buttonContainer);
        FrameLayout customButton = (FrameLayout) getLayoutInflater().inflate(R.layout.button_table, content, false);

        Button btn = customButton.findViewById(R.id.custom_table_btn);
        btn.setOnClickListener(v -> {
            WorkSpaceFragment contentFragment = WorkSpaceFragment.newInstance(project);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, contentFragment)
                    .addToBackStack(null)
                    .commit();
        });

        ImageView img = customButton.findViewById(R.id.custom_table_img);
        img.setBackgroundResource(Parser.getAvatarResource(project.getAvatarID()));

        TextView name = customButton.findViewById(R.id.custom_table_displayName);
        name.setText(project.getProjectName());
        content.addView(customButton);
    }

    private void setupShowUserInfo(){
        Button showUserInfo = projectView.findViewById(R.id.btn_show_user_info);
        ImageView user_img = projectView.findViewById(R.id.img_show_user_info);
        user_img.setBackgroundResource(Parser.getAvatarResource(user.getAvatarID()));
        // Add new button on click
        showUserInfo.setOnClickListener(v -> {
//            MyCustomLog.Toast(this,"Show user info");
            showUserInfo();
        });
    }

    private void showUserInfo(){
        View view = showBottomSheetDialog(R.layout.screen_user);

        ImageView img = view.findViewById(R.id.user_user_img);
        img.setBackgroundResource(Parser.getAvatarResource(user.getAvatarID()));

        TextView name = view.findViewById(R.id.username);
        TextView mail = view.findViewById(R.id.email);

        User userData = Data.currentUser;

        name.setText(userData.getFullName());
        mail.setText(userData.getEmail());

        Button btnSendFeedback = view.findViewById(R.id.btn_send_feedback);
        Button btnRateUs = view.findViewById(R.id.btn_rate_us);
        Button btnLogout = view.findViewById(R.id.btn_logout);
        Button btnDeleteAccount = view.findViewById(R.id.btn_delete_account);

        btnSendFeedback.setOnClickListener(v1 -> {

        });

        btnRateUs.setOnClickListener(v12 -> {

        });

        btnLogout.setOnClickListener(v13 -> {
            // logout tài khoản hiện tại đang dùng
            // clear hết pref đã lưu
            // clear hết data đang dùng
            // quay về sign in/ sign up
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnDeleteAccount.setOnClickListener(v14 -> {
            // hỏi lại lần nữa là có muốn xoá accoutn không
            // sau đó nhập mật khẩu để xác nhận
            // gửi lên server là xoá account này
            // sau đó quay về màn hình sign in/sign up

            View deleteScreen = showBottomSheetDialog(R.layout.buttomdialog_deleteaccount);

            TextView tvName = deleteScreen.findViewById(R.id.tvName);
            TextView tvMail = deleteScreen.findViewById(R.id.tvEmail);
            ImageButton close = deleteScreen.findViewById(R.id.btnClose);
            Button delete = deleteScreen.findViewById(R.id.btnDelete);

            tvName.setText(userData.getFullName());
            tvMail.setText(userData.getEmail());

            close.setOnClickListener(v141 -> {
                // delete
                curBottomDialog.dismiss();
            });

            delete.setOnClickListener(v142 -> {
                // delete

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });

        });
    }

    private void loadFragment(Fragment fragment ) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void goBackToPreviousFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // out activity
            finish();
        }
    }

    private View showBottomSheetDialog(@LayoutRes int resource) {
        // Khởi tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        curBottomDialog = bottomSheetDialog;
        // Nạp layout cho BottomSheetDialog
        View bottomSheetView = getLayoutInflater().inflate(resource, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        // Hiển thị Bottom Sheet
        bottomSheetDialog.show();
        return bottomSheetView;
    }

    private void setupSearchProjectByName(){
        EditText searchBtn = projectView.findViewById(R.id.etSearch);
        if (searchBtn == null) {
            MyCustomLog.DebugLog("EditText Check", "searchBtn is null");
        } else {
            searchBtn.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    MyCustomLog.DebugLog("TextWatcher", "beforeTextChanged: " + s);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    MyCustomLog.DebugLog("TextWatcher", "onTextChanged: " + s);
                    String query = searchBtn.getText().toString();

                    if (Objects.equals(query, "")) {
                        Data data = Data.getInstance();
                        for (Project project : data.getProjectsByUserId(user.getUserId())) {
                            LinearLayout prjContainer = projectView.findViewById(R.id.buttonContainer);
                            prjContainer.removeAllViews();
                            createButton(project);
                        }
                    } else {
                        List<Project> projectsSearched = searchProjectByName(query);

                        LinearLayout prjContainer = projectView.findViewById(R.id.buttonContainer);
                        prjContainer.removeAllViews();
                        for (Project project : projectsSearched) {
                            createButton(project);
                        }

                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    MyCustomLog.DebugLog("TextWatcher", "afterTextChanged: " + s.toString());
                }
            });
        }
    }

    private List<Project> searchProjectByName(String query){
        Data data = Data.getInstance();
        List<Project> matchedProjects = new ArrayList<>();

        for (Project project : data.getProjectsByUserId(user.getUserId())) {
            if (project.getProjectName().toLowerCase().contains(query.toLowerCase())) {
                matchedProjects.add(project);
            }
        }

        return matchedProjects;
    }
}

