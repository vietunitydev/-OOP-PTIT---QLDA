package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.Data.Data;
import com.example.qlda.Data.Parser;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.TimeGroup;
import com.example.qlda.Data.User;
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


public class IssueActivity extends AppCompatActivity {
    private FrameLayout viewContainer;
    private BottomSheetDialog curBottomDialog;
    View issueView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        viewContainer = findViewById(R.id.viewContainer);
        user = Data.currentUser;
        issueView = getLayoutInflater().inflate(R.layout.issue_dashboard, null);
        viewContainer.removeAllViews();
        viewContainer.addView(issueView);

        setupScreenIssueDetail();
    }
    private void setupScreenIssueDetail() {
        setupShowUserInfoIssue();
        searchIssue();
        setupShowListTask();

    }

    // Hàm tạo issue


    private void setupShowListTask(){
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

                itemIssue.setOnClickListener(v->{
                    ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(task);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, contentFragment)
                            .addToBackStack(null)
                            .commit();
//                    loadFragment(contentFragment);
                });

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
    private void searchIssue() {

    }
    private void searchCreateTask() {

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
}
