package com.example.qlda.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.qlda.Data.AppData;
import com.example.qlda.Data.Data;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.TableData;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.User;
import com.example.qlda.Data.TimeGroup;
import com.example.qlda.Data.UserData;
import com.example.qlda.Data.WorkListPageData;
import com.example.qlda.R;
import com.example.qlda.login.LoginActivity;
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
        setupCreateIssue();
        setupScreenIssue();
        searchIssue();
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



    // Code của Hải

    //Giao diện hiện thị các issue theo mốc thời gian

    // Hàm lấy một số ví dụ các Issue qua từng mốc thời gian
    private List<TimeGroup> getGroupedProjects() {
        List<TimeGroup> timeGroups = new ArrayList<>();

        // Dự liệu mẫu của các dự án
        List<Task> todayTask = new ArrayList<>();
        todayTask.add(new Task(1, "Project A", "Description A", 1 , 1, "Low", "Todo","2024-11-19"));
        todayTask.add(new Task(2, "Project B", "Description B", 2 , 2, "High", "Todo","2024-11-19"));

        List<Task> yesterdayTask = new ArrayList<>();
        yesterdayTask.add(new Task(3, "Project C", "Description C", 3 , 3, "Low", "Todo","2024-11-18"));

        List<Task> olderTask = new ArrayList<>();
        olderTask.add(new Task(4, "Project D", "Description D", 4 , 4, "Low", "Todo","2024-11-10"));

        // Thêm các nhóm vào danh sách
        timeGroups.add(new TimeGroup("Hôm nay", todayTask));
        timeGroups.add(new TimeGroup("Hôm qua", yesterdayTask));
        timeGroups.add(new TimeGroup("Cũ hơn", olderTask));

        return timeGroups;
    }
    private void setupScreenIssue() {
        // Lấy LinearLayout chứa các dự án
        LinearLayout parentLayout = issueView.findViewById(R.id.issueWithinTime);

        // Lấy các nhóm dự án phân theo thời gian
        List<TimeGroup> timeGroups = getGroupedProjects();

        // Kiểm tra nếu danh sách nhóm rỗng hoặc null
        if (timeGroups == null || timeGroups.isEmpty()) {
            return;
        }

        // Duyệt qua từng nhóm thời gian và thêm chúng vào giao diện
        for (TimeGroup timeGroup : timeGroups) {
            // Nếu không có dự án nào trong nhóm này, bỏ qua nhóm đó
            if (timeGroup.getProjects() == null || timeGroup.getProjects().isEmpty()) {
                continue;
            }

            // Inflate layout cho từng nhóm thời gian
            View sectionView = LayoutInflater.from(issueView.getContext())
                    .inflate(R.layout.item_time_section, parentLayout, false);

            // Cập nhật tiêu đề nhóm thời gian
            TextView timeHeader = sectionView.findViewById(R.id.sectionTimeHeader);
            timeHeader.setText(timeGroup.getTimeLabel());

            // Thêm các dự án vào nhóm thời gian
            LinearLayout projectList = sectionView.findViewById(R.id.projectList);
            for (Task task : timeGroup.getProjects()) {
                View itemIssue = LayoutInflater.from(issueView.getContext())
                        .inflate(R.layout.item_issue, projectList, false);

                // Thêm dữ liệu vào item issue
                TextView item_Issue_txtName = itemIssue.findViewById(R.id.item_Issue_txtName);
                item_Issue_txtName.setText(String.valueOf(task.getTaskId()));

                TextView item_Issue_itemName = itemIssue.findViewById(R.id.item_Issue_itemName);
                item_Issue_itemName.setText(task.getTaskName());

                TextView item_Issue_Status = itemIssue.findViewById(R.id.item_Issue_Status);
                item_Issue_Status.setText(task.getStatus());

//                Button projectButton = (Button) LayoutInflater.from(this).inflate(R.layout.issue_details, projectList, false);
//
//                // Xử lý sự kiện khi nhấn vào dự án
//                projectButton.setOnClickListener(v -> {
//                    Intent intent = new Intent(this, DetailIssue.class);
//                    startActivity(intent);
//                });

                projectList.addView(itemIssue);
            }

            // Thêm nhóm thời gian vào layout cha
            parentLayout.addView(sectionView);
        }
    }

    // Hàm tạo issue

    private void setupCreateIssue(){
        ImageButton imgBtnAddIssue = issueView.findViewById(R.id.imgBtnAddIssues);

        imgBtnAddIssue.setOnClickListener(v -> {
            View view = showBottomSheetDialog(R.layout.screen_create_issue);
        });
    }
    private void searchIssue() {
        ImageButton imgBtnSearch = issueView.findViewById(R.id.imgBtnSearch);
        EditText edtSearch = issueView.findViewById(R.id.edtSearch);

        imgBtnSearch.setOnClickListener(v -> {
            if (edtSearch.getVisibility() == View.GONE) {
                edtSearch.setVisibility(View.VISIBLE);
            } else {
                edtSearch.setVisibility(View.GONE);
            }
        });

        // Thêm logic để ẩn EditText khi nhấn ra ngoài
        issueView.setOnTouchListener((v, event) -> {
            if (edtSearch.getVisibility() == View.VISIBLE) {
                edtSearch.setVisibility(View.GONE);
            }
            return false; // Cho phép các sự kiện khác tiếp tục xử lý
        });
    }
}

