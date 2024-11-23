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

    private LayoutInflater inflater;
    private FrameLayout viewContainer;

    // 3 view will use
    View projectView;
    View notificationView;
    View issueView;

    User user;

    private BottomSheetDialog curBottomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        // Initialize LayoutInflater and LinearLayout
        inflater = LayoutInflater.from(this);
        viewContainer = findViewById(R.id.viewContainer);

        user = Data.currentUser;

        initializeButtons();
        fetchUserProject();
        setupCreateProject();
        setupShowUserInfo();

        setupScreenIssueDetail();
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

//        appData.fetchUser(() ->{
//            // fetch Data for app
//            appData.fetchData(() -> {
//                tables = AppData.Tables;
//
//                MyCustomLog.DebugLog("FireBase Store", String.format("Fetched Data Successfully %d", tables.size()));
//                for (TableData data : tables) {
//                    createButton(data);
//                }
//            });
//        });
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
//                TableData newTable = new TableData("table-id-" + fs.getNewIDTable(),projectName, "#AAAAAA", new Date());
//                WorkListPageData newPage = new WorkListPageData("page-id-" + fs.getNewIDTable(),newTable.getId(),"New Page List",new Date());
//                newTable.addWorkListPage(newPage);

                Project newProject = new Project(20, "Game Development", "Description 4", "2024-05-01", "2024-11-30", "Ongoing", 1);
                createButton(newProject);

                // add vao APPDATA
//                AppData.addTable(newTable);
//                MyCustomLog.DebugLog("Debug DATA", AppData.convertToJson(AppData.Tables));

                curBottomDialog.dismiss();
                updateUI();
            });

        });
    }
    private void createButton(Project project) {
        LinearLayout content = projectView.findViewById(R.id.buttonContainer);
        FrameLayout customButton = (FrameLayout) inflater.inflate(R.layout.button_table, content, false);

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
        // Hiển thị Bottom Sheet
        bottomSheetDialog.show();
        return bottomSheetView;
    }

    private void setupScreenIssueDetail() {

        setupShowUserInfoIssue();
        searchIssue();
        setupSearchProjectByName();


        // Lấy LinearLayout chứa các dự án
        LinearLayout parentLayout = issueView.findViewById(R.id.issueWithinTime);

        for (TimeGroup timeGroup : getGroupedProjects()) {
            if (timeGroup.getProjects() == null || timeGroup.getProjects().isEmpty()) {
                continue;
            }

            View sectionView = LayoutInflater.from(issueView.getContext())
                    .inflate(R.layout.item_time_section, parentLayout, false);

            TextView timeHeader = sectionView.findViewById(R.id.sectionTimeHeader);
            timeHeader.setText(timeGroup.getTimeLabel());

            LinearLayout projectList = sectionView.findViewById(R.id.projectList);
            for (Task task : timeGroup.getProjects()) {
                View itemIssue = LayoutInflater.from(issueView.getContext())
                        .inflate(R.layout.item_issue, projectList, false);

                TextView item_Issue_txtName = itemIssue.findViewById(R.id.item_Issue_txtName);
                item_Issue_txtName.setText(task.getTaskName());

                ImageView item_Issue_img = itemIssue.findViewById(R.id.item_Issue_img);
                item_Issue_img.setBackgroundResource(Parser.getTaskTypeResource(task.getTaskType()));

                ImageView avt_priority = itemIssue.findViewById(R.id.avt_priority);
                avt_priority.setBackgroundResource(Parser.getPriorityTypeResource(task.getPriority()));

                ImageView avt_assignee = itemIssue.findViewById(R.id.avt_assignee);
                avt_assignee.setBackgroundResource(Parser.getAvatarResource(task.getAssignedTo()));

                TextView item_Issue_Status = itemIssue.findViewById(R.id.item_Issue_Status);
                item_Issue_Status.setText(task.getStatus().toString());

                projectList.addView(itemIssue);
            }

            parentLayout.addView(sectionView);
        }
    }

    // Hàm tạo issue
    private void searchIssue() {

    }

    private List<TimeGroup> getGroupedProjects() {
        List<TimeGroup> timeGroups = new ArrayList<>();

        List<Task> projectTask = Data.getInstance().getAllTasks();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate startOfThisWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfThisWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);
        LocalDate startOfLastWeek = startOfThisWeek.minusWeeks(1);
        LocalDate endOfLastWeek = endOfThisWeek.minusWeeks(1);
        LocalDate startOfThisMonth = today.withDayOfMonth(1);

        List<Task> todayTasks = new ArrayList<>();
        List<Task> yesterdayTasks = new ArrayList<>();
        List<Task> thisWeekTasks = new ArrayList<>();
        List<Task> lastWeekTasks = new ArrayList<>();
        List<Task> thisMonthTasks = new ArrayList<>();
        Map<String, List<Task>> otherMonthTasks = new HashMap<>();

        // Phân loại task theo thời gian
        for (Task task : projectTask) {
            LocalDate taskDate = task.getCreateDateAsLocalDate();

            if (taskDate.equals(today)) {
                todayTasks.add(task);
            } else if (taskDate.equals(yesterday)) {
                yesterdayTasks.add(task);
            } else if (!taskDate.isBefore(startOfThisWeek) && !taskDate.isAfter(endOfThisWeek)) {
                thisWeekTasks.add(task);
            } else if (!taskDate.isBefore(startOfLastWeek) && !taskDate.isAfter(endOfLastWeek)) {
                lastWeekTasks.add(task);
            } else if (!taskDate.isBefore(startOfThisMonth)) {
                thisMonthTasks.add(task);
            } else {
                Month taskMonth = taskDate.getMonth();
                int taskYear = taskDate.getYear();
                String monthLabel = taskMonth + " " + taskYear;
                otherMonthTasks.putIfAbsent(monthLabel, new ArrayList<>());
                otherMonthTasks.get(monthLabel).add(task);
            }
        }

        // Thêm các nhóm thời gian vào danh sách kết quả nếu có task
        if (!todayTasks.isEmpty()) {
            timeGroups.add(new TimeGroup("Today", todayTasks));
        }
        if (!yesterdayTasks.isEmpty()) {
            timeGroups.add(new TimeGroup("Yesterday", yesterdayTasks));
        }
        if (!thisWeekTasks.isEmpty()) {
            timeGroups.add(new TimeGroup("This Week", thisWeekTasks));
        }
        if (!lastWeekTasks.isEmpty()) {
            timeGroups.add(new TimeGroup("Last Week", lastWeekTasks));
        }
        if (!thisMonthTasks.isEmpty()) {
            timeGroups.add(new TimeGroup("This Month", thisMonthTasks));
        }

        for (Map.Entry<String, List<Task>> entry : otherMonthTasks.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                timeGroups.add(new TimeGroup(entry.getKey(), entry.getValue()));
            }
        }

        return timeGroups;
    }
    private void setupShowUserInfoIssue(){
        FrameLayout showUserInfo = issueView.findViewById(R.id.btn_info);
        ImageView imageView = issueView.findViewById(R.id.img_show_user_info);
        imageView.setBackgroundResource(Parser.getAvatarResource(user.getAvatarID()));
        // Add new button on click
        showUserInfo.setOnClickListener(v -> {
            showUserInfo();
        });
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

