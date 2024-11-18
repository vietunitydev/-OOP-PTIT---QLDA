package com.example.qlda.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.qlda.Data.AppData;
import com.example.qlda.Data.Data;
import com.example.qlda.Data.TableData;
import com.example.qlda.Data.User;
import com.example.qlda.Data.UserData;
import com.example.qlda.Data.WorkListPageData;
import com.example.qlda.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private FrameLayout viewContainer;

    // 3 view will use
    View projectView;
    View notificationView;
    View issueView;

    private List<TableData> tables = new ArrayList<>();

    AppData appData = new AppData();

    private BottomSheetDialog curBottomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        // Initialize LayoutInflater and LinearLayout
        inflater = LayoutInflater.from(this);
        viewContainer = findViewById(R.id.viewContainer);

        initializeButtons();
        fetchUserProject();
        setupCreateProject();
        setupShowUserInfo();

    }

    // add function show for 3 button
    public void initializeButtons() {
        Button btnProject = findViewById(R.id.btn_Projects);
        Button btnNotification = findViewById(R.id.btnNotification);
        Button btnIssue = findViewById(R.id.btn_Issue);

        LayoutInflater inflater = LayoutInflater.from(this);
        projectView = inflater.inflate(R.layout.activity_project, null);
        notificationView = inflater.inflate(R.layout.screen_user, null);
        issueView = inflater.inflate(R.layout.issue_dashboard, null);

        setActiveView(projectView);

        btnProject.setOnClickListener(v -> setActiveView(projectView));
        btnNotification.setOnClickListener(v -> setActiveView(notificationView));
        btnIssue.setOnClickListener(v -> setActiveView(issueView));
    }
    private void setActiveView(View view) {
        viewContainer.removeAllViews();
        viewContainer.addView(view);
    }

    //=========================================================================

    private void fetchUserProject(){
        //        appData.InitTable();

        appData.fetchUser(() ->{
            // fetch Data for app
            appData.fetchData(() -> {
                tables = AppData.Tables;

                MyCustomLog.DebugLog("FireBase Store", String.format("Fetched Data Successfully %d", tables.size()));
                for (TableData data : tables) {
                    createButton(data);
                }
            });
        });
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

                FireStoreHelper fs = new FireStoreHelper();
                MyCustomLog.Toast(this,"Click Add Table Button");
                TableData newTable = new TableData("table-id-" + fs.getNewIDTable(),projectName, "#AAAAAA", new Date());
                WorkListPageData newPage = new WorkListPageData("page-id-" + fs.getNewIDTable(),newTable.getId(),"New Page List",new Date());
                newTable.addWorkListPage(newPage);
                createButton(newTable);

                // add vao APPDATA
                AppData.addTable(newTable);
                MyCustomLog.DebugLog("Debug DATA", AppData.convertToJson(AppData.Tables));

                curBottomDialog.dismiss();
                updateUI();
            });

        });
    }

    private void createButton(TableData table) {
        LinearLayout content = projectView.findViewById(R.id.buttonContainer);
        FrameLayout customButton = (FrameLayout) inflater.inflate(R.layout.button_table, content, false);

        Button btn = customButton.findViewById(R.id.custom_table_btn);
        btn.setOnClickListener(v -> {
            WorkSpaceFragment contentFragment = WorkSpaceFragment.newInstance(table);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, contentFragment)
                    .addToBackStack(null)
                    .commit();
        });

        ImageView img = customButton.findViewById(R.id.custom_table_img);
        img.setBackgroundColor(Color.parseColor(table.getColor()));

        TextView name = customButton.findViewById(R.id.custom_table_displayName);
        name.setText(table.getTitle());
        content.addView(customButton);
    }

    private void setupShowUserInfo(){
        Button showUserInfo = projectView.findViewById(R.id.btn_show_user_info);
        // Add new button on click
        showUserInfo.setOnClickListener(v -> {
//            MyCustomLog.Toast(this,"Show user info");
            View view = showBottomSheetDialog(R.layout.screen_user);

            Integer id_user = 1;

            TextView name = view.findViewById(R.id.username);
            TextView mail = view.findViewById(R.id.email);

            User userData = Data.getInstance().getUserById(id_user);

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
            });

            btnDeleteAccount.setOnClickListener(v14 -> {
                // hỏi lại lần nữa là có muốn xoá accoutn không
                // sau đó nhập mật khẩu để xác nhận
                // gửi lên server là xoá account này
                // sau đó quay về màn hình sign in/sign up

            });
        });
    }

    private void searchProjectByName(String name){

    }

    private void updateUI(){
//        tables = AppData.Tables;
//        viewContainer.removeAllViews();
//        // ve lai
//        for (TableData data : tables) {
//            createButton(data);
//        }
    }

    private void loadFragment(Fragment fragment ) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void goBackToPreviousFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            updateUI();
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

//        // Ánh xạ các view trong bottom sheet nếu cần xử lý sự kiện
//        Button btnProfile = bottomSheetView.findViewById(R.id.btnProfile);
//        Button btnSettings = bottomSheetView.findViewById(R.id.btnSettings);
//        Button btnLogout = bottomSheetView.findViewById(R.id.btnLogout);
//
//        // Xử lý sự kiện khi bấm các nút trong Bottom Sheet
//        btnProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Thực hiện hành động khi nhấn vào "View Profile"
//                // Đóng BottomSheetDialog
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        btnSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Thực hiện hành động khi nhấn vào "Settings"
//                // Đóng BottomSheetDialog
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Thực hiện hành động khi nhấn vào "Logout"
//                // Đóng BottomSheetDialog
//                bottomSheetDialog.dismiss();
//            }
//        });

        // Hiển thị Bottom Sheet
        bottomSheetDialog.show();
        return bottomSheetView;
    }
}

