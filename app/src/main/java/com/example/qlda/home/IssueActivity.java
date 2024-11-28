package com.example.qlda.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qlda.Data.Data;
import com.example.qlda.Data.Parser;
import com.example.qlda.Data.Priority;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.StatusType;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.TaskType;
import com.example.qlda.Data.TimeGroup;
import com.example.qlda.Data.User;
import com.example.qlda.R;
import com.example.qlda.Utils.TimeUtils;
import com.example.qlda.login.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class IssueActivity extends AppCompatActivity {
    private FrameLayout viewContainer;
    private BottomSheetDialog curBottomDialog;
    View issueView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        user = Data.currentUser;

        initializeButtons();
        setupScreenIssueDetail();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                setupShowListTask();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void initializeButtons() {
        viewContainer = findViewById(R.id.viewContainer);

        Button btnProject = findViewById(R.id.btn_Projects);
        Button btnNotification = findViewById(R.id.btnNotification);

        issueView = getLayoutInflater().inflate(R.layout.issue_dashboard, null);
        viewContainer.removeAllViews();
        viewContainer.addView(issueView);

        btnNotification.setOnClickListener(v -> {

        });
        btnProject.setOnClickListener(v ->{
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupScreenIssueDetail() {
        setupShowUserInfoIssue();
        setupSearchIssue();
        setupShowListTask();
        setupCreateNewIssue();
    }

    // Hàm tạo issue


    private void setupShowListTask(){
        LinearLayout parentLayout = issueView.findViewById(R.id.issueWithinTime);
        parentLayout.removeAllViews();

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
                getCorrectStatusView(item_Issue_Status,task);

                projectList.addView(itemIssue);
            }

            parentLayout.addView(sectionView);
        }
    }
    private void setupSearchIssue() {
        FrameLayout btn_Search = issueView.findViewById(R.id.btn_Search);
        btn_Search.setOnClickListener(v-> {

        });

        TextView searchProject = issueView.findViewById(R.id.project);
        searchProject.setOnClickListener(v->{
            View searchProjectView = showBottomSheetDialog(R.layout.bottomdialog_chooseproject);


            EditText searchText = searchProjectView.findViewById(R.id.search_people);
            searchText.setVisibility(View.GONE);
            // show selected
            TextView selectedText = searchProjectView.findViewById(R.id.selected_text);
            LinearLayout selected = searchProjectView.findViewById(R.id.selected);
            selectedText.setVisibility(View.GONE);
            selected.setVisibility(View.GONE);


            LinearLayout suggestion_container = searchProjectView.findViewById(R.id.suggestion_container);
            suggestion_container.removeAllViews();

            List<Project> newProjects = null;
            try {
                newProjects = Data.getInstance().getProjectsByUserId(user.getUserId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < newProjects.size(); i++) {

                Project project = newProjects.get(i);

                View prjBtn = getLayoutInflater().inflate(R.layout.button_chooseproject, null);
                ImageView btn_project_avt = prjBtn.findViewById(R.id.btn_project_avt);
                TextView btn_project_username = prjBtn.findViewById(R.id.btn_project_username);

                btn_project_avt.setBackgroundResource(Parser.getAvatarResource(project.getAvatarID()));
                btn_project_username.setText(project.getProjectName());

                suggestion_container.addView(prjBtn,i);

                prjBtn.setOnClickListener(v1 -> {
                    searchIssue(SearchType.project, String.valueOf(project.getProjectId()));
                    curBottomDialog.dismiss();
                });

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) prjBtn.getLayoutParams();
                if (params != null) {
                    params.setMargins(0, 8, 0, 8);
                    prjBtn.setLayoutParams(params);
                }
            }
        });

        TextView type = issueView.findViewById(R.id.type);
        type.setOnClickListener(v->{
            View typeTaskView = showBottomSheetDialog(R.layout.bottomdialog_changetasktype);
            LinearLayout taskView = typeTaskView.findViewById(R.id.task);
            taskView.setOnClickListener(v12->{
                // search type
                searchIssue(SearchType.type, TaskType.Task.toString());
                curBottomDialog.dismiss();

            });
            LinearLayout bugView = typeTaskView.findViewById(R.id.bug);
            bugView.setOnClickListener(v12->{
                // search type
                searchIssue(SearchType.type, TaskType.Bug.toString());
                curBottomDialog.dismiss();

            });
            LinearLayout storyView = typeTaskView.findViewById(R.id.story);
            storyView.setOnClickListener(v12->{
                // search type
                searchIssue(SearchType.type, TaskType.Story.toString());
                curBottomDialog.dismiss();
            });
        });

        TextView status = issueView.findViewById(R.id.status);
        status.setOnClickListener(v->{
            View statusView = showBottomSheetDialog(R.layout.bottomdialog_selectstatus);
            LinearLayout todo = statusView.findViewById(R.id.btn_todo);
            todo.setOnClickListener(v1-> {
                searchIssue(SearchType.status, StatusType.Todo.toString());
                curBottomDialog.dismiss();
            });
            // btn2
            LinearLayout inProgress = statusView.findViewById(R.id.btn_inprogress);
            inProgress.setOnClickListener(v1-> {
                searchIssue(SearchType.status, StatusType.InProgress.toString());
                curBottomDialog.dismiss();
            });// btn3
            LinearLayout done = statusView.findViewById(R.id.btn_done);
            done.setOnClickListener(v1-> {
                searchIssue(SearchType.status, StatusType.Done.toString());
                curBottomDialog.dismiss();
            });
        });

        TextView priority = issueView.findViewById(R.id.priority);
        priority.setOnClickListener(v->{
            View priorityView = showBottomSheetDialog(R.layout.bottomdialog_selectpriority);

            LinearLayout todo = priorityView.findViewById(R.id.btn_low);
            todo.setOnClickListener(v1-> {
                searchIssue(SearchType.priority, Priority.Low.toString());
                curBottomDialog.dismiss();
            });
            // btn2
            LinearLayout inProgress = priorityView.findViewById(R.id.btn_medium);
            inProgress.setOnClickListener(v1-> {
                searchIssue(SearchType.priority, Priority.Medium.toString());
                curBottomDialog.dismiss();
            });// btn3
            LinearLayout done = priorityView.findViewById(R.id.btn_high);
            done.setOnClickListener(v1-> {
                searchIssue(SearchType.priority, Priority.High.toString());
                curBottomDialog.dismiss();
            });
        });

//        TextView assignee = issueView.findViewById(R.id.assignee);
//        assignee.setOnClickListener(v->{
//            View assigneeView = showBottomSheetDialog(R.layout.bottomdialog_assignee);
//
//            EditText searchText = assigneeView.findViewById(R.id.search_people);
//            searchText.setVisibility(View.GONE);
//            // show selected
//            TextView selectedText = assigneeView.findViewById(R.id.selected_text);
//            LinearLayout selected = assigneeView.findViewById(R.id.selected);
//            selectedText.setVisibility(View.GONE);
//            selected.setVisibility(View.GONE);
//
//            List<User> users = Data.getInstance().Get(project.getProjectId());
//        });
//
//        TextView reporter = issueView.findViewById(R.id.reporter);
//        reporter.setOnClickListener(v->{
//            View reporterView = showBottomSheetDialog(R.layout.bottomdialog_assignee);
//
//            EditText searchText = reporterView.findViewById(R.id.search_people);
//            searchText.setVisibility(View.GONE);
//            // show selected
//            TextView selectedText = reporterView.findViewById(R.id.selected_text);
//            LinearLayout selected = reporterView.findViewById(R.id.selected);
//            selectedText.setVisibility(View.GONE);
//            selected.setVisibility(View.GONE);
//        });

        TextView remove = issueView.findViewById(R.id.remove);

        remove.setOnClickListener(v->{
            setupShowListTask();
        });
    }

    private void searchIssue(SearchType type, String value) {
        List<Task> filteredTasks = new ArrayList<>();
        List<Task> tasks = null;
        try {
            tasks = Data.getInstance().getTasksByUserOwner(Data.currentUser.getUserId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Task task : tasks) {
            boolean matches = true;
            switch (type) {
                case none:
                    break;

                case project:
                    if (!(task.getProjectId() == Integer.parseInt(value))) {
                        matches = false;
                    }
                    break;

                case type:
                    if (!task.getTaskType().toString().equalsIgnoreCase(value)) {
                        matches = false;
                    }
                    break;

                case status:
                    if (!task.getStatus().toString().equalsIgnoreCase(value)) {
                        matches = false;
                    }
                    break;

                case priority:
                    if (!task.getPriority().toString().equalsIgnoreCase(value)) {
                        matches = false;
                    }
                    break;

                case assignee:
                    if (!String.valueOf(task.getAssignedTo()).equals(value)) {
                        matches = false;
                    }
                    break;

                case reporter:
                    if (!String.valueOf(task.getReporter()).equals(value)) {
                        matches = false;
                    }
                    break;
            }

            if (matches) {
                filteredTasks.add(task);
            }
        }

        LinearLayout parentLayout = issueView.findViewById(R.id.issueWithinTime);
        parentLayout.removeAllViews();

        for (Task task : filteredTasks) {
            View itemIssue = LayoutInflater.from(issueView.getContext())
                    .inflate(R.layout.item_issue, parentLayout, false);

            itemIssue.setOnClickListener(v->{
                ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(task);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, contentFragment)
                        .addToBackStack(null)
                        .commit();
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
            getCorrectStatusView(item_Issue_Status,task);

            parentLayout.addView(itemIssue);
        }
    }


    private void setupCreateNewIssue() {
        ImageView btnAdd = issueView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v->{

            List<Project> projects = null;
            try {
                projects = Data.getInstance().getProjectsByUserId(user.getUserId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(projects.size() == 0){
                showPopup("Không có project nào", () ->{
                    // make any thing ....
                });
                return;
            }

            Project tempProject = projects.get(0).Clone();

            Task tempTask = new Task();

            View createIssueView = showBottomSheetDialog(R.layout.screen_create_issue);

            //////// choose project
            ImageView img_chooseProject = createIssueView.findViewById(R.id.img_chooseProject);
            TextView name_chooseProject = createIssueView.findViewById(R.id.name_chooseProject);
            img_chooseProject.setBackgroundResource(Parser.getAvatarResource(tempProject.getAvatarID()));
            name_chooseProject.setText(tempProject.getProjectName());

            LinearLayout btn_chooseProject = createIssueView.findViewById(R.id.btn_chooseProject);
            btn_chooseProject.setOnClickListener(v11->{
                View changeTaskView = showBottomSheetDialog(R.layout.bottomdialog_chooseproject);

                // show selected
                LinearLayout selected = changeTaskView.findViewById(R.id.selected);
                ImageView avt_selected = selected.findViewById(R.id.avt_selected);
                TextView username_selected = selected.findViewById(R.id.name_selected);

                avt_selected.setBackgroundResource(Parser.getAvatarResource(tempProject.getAvatarID()));
                username_selected.setText(tempProject.getProjectName());

                LinearLayout suggestion_container = changeTaskView.findViewById(R.id.suggestion_container);
                suggestion_container.removeAllViews();

                List<Project> newProjects = null;
                try {
                    newProjects = Data.getInstance().getProjectsByUserId(user.getUserId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newProjects.removeIf(prj -> prj.getProjectId() == tempProject.getProjectId());

                for (int i = 0; i < newProjects.size(); i++) {

                    Project project = newProjects.get(i);

                    View prjBtn = getLayoutInflater().inflate(R.layout.button_chooseproject, null);
                    ImageView btn_project_avt = prjBtn.findViewById(R.id.btn_project_avt);
                    TextView btn_project_username = prjBtn.findViewById(R.id.btn_project_username);

                    btn_project_avt.setBackgroundResource(Parser.getAvatarResource(project.getAvatarID()));
                    btn_project_username.setText(project.getProjectName());

                    suggestion_container.addView(prjBtn,i);

                    prjBtn.setOnClickListener(v111 -> {
                        tempTask.setProjectId(project.getProjectId());
                        curBottomDialog.dismiss();
                        tempProject.SetClone(project);
                        //update UI. tạm thời
                        img_chooseProject.setBackgroundResource(Parser.getAvatarResource(tempProject.getAvatarID()));
                        name_chooseProject.setText(tempProject.getProjectName());
                    });

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) prjBtn.getLayoutParams();
                    if (params != null) {
                        params.setMargins(0, 8, 0, 8);
                        prjBtn.setLayoutParams(params);
                    }
                }
            });
            ////////////////////////////////////

            //////// choose task
            ImageView img_chooseTask = createIssueView.findViewById(R.id.img_chooseTask);
            TextView name_chooseTask = createIssueView.findViewById(R.id.name_chooseTask);
            img_chooseTask.setBackgroundResource(Parser.getTaskTypeResource(TaskType.Task));
            name_chooseTask.setText(TaskType.Task.toString());
            tempTask.setTaskType(TaskType.Task);

            LinearLayout btn_chooseTask = createIssueView.findViewById(R.id.btn_chooseTask);
            btn_chooseTask.setOnClickListener(v11->{
                View changeTaskView = showBottomSheetDialog(R.layout.bottomdialog_changetasktype);

                LinearLayout taskView = changeTaskView.findViewById(R.id.task);
                taskView.setOnClickListener(v12->{
                    tempTask.setTaskType(TaskType.Task);
                    img_chooseTask.setBackgroundResource(Parser.getTaskTypeResource(TaskType.Task));
                    name_chooseTask.setText(TaskType.Task.toString());
                    curBottomDialog.dismiss();

                });
                LinearLayout bugView = changeTaskView.findViewById(R.id.bug);
                bugView.setOnClickListener(v12->{
                    tempTask.setTaskType(TaskType.Bug);
                    img_chooseTask.setBackgroundResource(Parser.getTaskTypeResource(TaskType.Bug));
                    name_chooseTask.setText(TaskType.Bug.toString());
                    curBottomDialog.dismiss();

                });
                LinearLayout storyView = changeTaskView.findViewById(R.id.story);
                storyView.setOnClickListener(v12->{
                    tempTask.setTaskType(TaskType.Story);
                    img_chooseTask.setBackgroundResource(Parser.getTaskTypeResource(TaskType.Story));
                    name_chooseTask.setText(TaskType.Story.toString());
                    curBottomDialog.dismiss();

                });
            });
            ////////////////////////////////////

            ///// Edit name create issue
            EditText edtNameIssue = createIssueView.findViewById(R.id.edtNameIssue);
            edtNameIssue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        // MyCustomLog.Toast(getBaseContext(), "Edit enter");

                        tempTask.setTaskName(edtNameIssue.toString());
                        v.clearFocus();

                        return true;
                    }
                    return false;
                }
            });

            ////////////////////////////////////

            ///////  DESCRIPTION
            FrameLayout btnDesIssue = createIssueView.findViewById(R.id.btnDesIssue);
            EditText textContentDescription = createIssueView.findViewById(R.id.textContentDescription);
            textContentDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        tempTask.setDescription(textContentDescription.toString());
                        v.clearFocus();
                        return true;
                    }
                    return false;
                }
            });
            ////////////////////////////////////

            ///////  ATTACHMENT
//            LinearLayout attachment = createIssueView.findViewById(R.id.attachment);
//            TextView add_attachment = createIssueView.findViewById(R.id.add_attachment);
            ////////////////////////////////////

            LinearLayout btnFieldIssue = createIssueView.findViewById(R.id.btnFieldIssue);
//            TextView textContentFields = createIssueView.findViewById(R.id.textContentFields);
            LinearLayout contentFields = createIssueView.findViewById(R.id.contentFields);

            ImageView avt_Assignee = createIssueView.findViewById(R.id.avt_Assignee);
            TextView name_Assignee = createIssueView.findViewById(R.id.name_Assignee);
            LinearLayout btn_Assignee = createIssueView.findViewById(R.id.btn_Assignee);
            btn_Assignee.setOnClickListener(v12 ->{
                try {
                    setupChangeUserAssignee(true,tempProject,tempTask,() ->{
                        avt_Assignee.setBackgroundResource(Parser.getAvatarResource(tempTask.getAssignedTo()));
                        try {
                            name_Assignee.setText(Data.getInstance().getUserById(tempTask.getAssignedTo()).getFullName());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            ImageView avt_Reporter = createIssueView.findViewById(R.id.avt_Reporter);
            TextView name_Reporter = createIssueView.findViewById(R.id.name_Reporter);
            LinearLayout btn_Reporter = createIssueView.findViewById(R.id.btn_Reporter);
            btn_Reporter.setOnClickListener(v12 ->{
                try {
                    setupChangeUserAssignee(false,tempProject,tempTask, () ->{
                        avt_Reporter.setBackgroundResource(Parser.getAvatarResource(tempTask.getReporter()));
                        try {
                            name_Reporter.setText(Data.getInstance().getUserById(tempTask.getReporter()).getFullName());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            TextView btn_Create = createIssueView.findViewById(R.id.btn_Create);
            btn_Create.setOnClickListener(v11 -> {
                if(Objects.equals(edtNameIssue.getText().toString(),"")){
                    showPopup("Vui lòng điền thêm thông tin",()->{});
                    return;
                }
                if(tempTask.getAssignedTo() == 0 || tempTask.getReporter() == 0){
//                    showPopup("Chưa add reporter và assign. Tự động gán cho bạn",()->{});
                    tempTask.setReporter(Data.currentUser.getUserId());
                    tempTask.setAssignedTo(Data.currentUser.getUserId());
                }

                MyCustomLog.Toast(this,"CREATE NEW TASK");
                Data.getInstance().createAndFetchTask(edtNameIssue.getText().toString(),textContentDescription.getText().toString(),tempTask.getAssignedTo(),tempTask.getReporter(),tempTask.getProjectId(),tempTask.getTaskType().toString(), tempTask.getPriority().toString(),tempTask.getStatus().toString(),"2024-11-24","2024-11-24");
                curBottomDialog.dismiss();
                setupShowListTask();
            });
        });
    }

    private List<TimeGroup> getGroupedProjects() {
        List<TimeGroup> timeGroups = new ArrayList<>();

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
        try {
            for (Task task : Data.getInstance().getTasksByUserOwner(Data.currentUser.getUserId())) {
                LocalDate taskDate = TimeUtils.stringToLocalDate(task.getCreateDate());

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    private void setupChangeUserAssignee(boolean isAssign, Project project, Task task,  Runnable callback) throws SQLException {
        View assign = showBottomSheetDialog(R.layout.bottomdialog_assignee);
        // search people
        EditText search_people = assign.findViewById(R.id.search_people);
        search_people.setVisibility(View.GONE);

        // show selected
        LinearLayout selected = assign.findViewById(R.id.selected);
        ImageView avt_selected = selected.findViewById(R.id.avt_selected);
        TextView username_selected = selected.findViewById(R.id.username_selected);
        selected.setVisibility(View.GONE);

        // find a list user in this project
        List<User> users = null;
        try {
            users = Data.getInstance().getUsersInProjectWithID(project.getProjectId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        User cur = Data.currentUser;

        LinearLayout suggestion_container = assign.findViewById(R.id.suggestion_container);
        suggestion_container.removeAllViews();

        for (int i = 0; i < users.size(); i++) {

            User user = users.get(i);
            View userBtn = getLayoutInflater().inflate(R.layout.button_assignee, null);

            ImageView btn_assignee_avt = userBtn.findViewById(R.id.btn_assignee_avt);
            TextView btn_assignee_username = userBtn.findViewById(R.id.btn_assignee_username);
            btn_assignee_avt.setBackgroundResource(Parser.getAvatarResource(user.getAvatarID()));
            if(cur.getUserId() == user.getUserId()){
                btn_assignee_username.setText("Me");
            }
            else{
                btn_assignee_username.setText(user.getFullName());
            }

            suggestion_container.addView(userBtn,i);

            userBtn.setOnClickListener(v -> {
                if(isAssign){
                    task.setAssignedTo(user.getUserId());
                }
                else{
                    task.setReporter(user.getUserId());
                }

                curBottomDialog.dismiss();
                //update UI. tạm thời
                callback.run();
            });

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) userBtn.getLayoutParams();
            if (params != null) {
                params.setMargins(0, 8, 0, 8);
                userBtn.setLayoutParams(params);
            }
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

    private void getCorrectStatusView(TextView view, Task task){
        if(task.getStatus() == StatusType.Todo){
            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray_2));
            view.setTextColor(ContextCompat.getColorStateList(this, R.color.black));
            view.setText("TO DO");
        }
        if(task.getStatus() == StatusType.InProgress){
            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_1));
            view.setTextColor(ContextCompat.getColorStateList(this, R.color.blue));
            view.setText("IN PROGRESS");
        }
        if(task.getStatus() == StatusType.Done){
            view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_low));
            view.setTextColor(ContextCompat.getColorStateList(this, R.color.green_high));
            view.setText("DONE");
        }

    }

    private void showPopup(String message,  Runnable action) {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (action != null) {
                        action.run();
                    }
                    dialog.dismiss();
                })
                .show();
    }

    public List<Task> searchTasks(List<Task> tasks, String taskName, Integer assignedTo, Integer reporter, TaskType taskType, Priority priority, StatusType status) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : tasks) {
            boolean matches = true;

            if (taskName != null && !taskName.isEmpty()) {
                if (!task.getTaskName().toLowerCase().contains(taskName.toLowerCase())) {
                    matches = false;
                }
            }

            if (assignedTo != null) {
                if (task.getAssignedTo() != assignedTo) {
                    matches = false;
                }
            }

            if (reporter != null) {
                if (task.getReporter() != reporter) {
                    matches = false;
                }
            }

            if (taskType != null) {
                if (task.getTaskType() != taskType) {
                    matches = false;
                }
            }

            if (priority != null) {
                if (task.getPriority() != priority) {
                    matches = false;
                }
            }

            if (status != null) {
                if (task.getStatus() != status) {
                    matches = false;
                }
            }

            if (matches) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }
    enum SearchType{
        none,
        project,
        type,
        status,
        priority,
        assignee,
        reporter
    }
}
