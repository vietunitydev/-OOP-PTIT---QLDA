package com.example.qlda.home;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.qlda.Data.Comment;
import com.example.qlda.Data.Data;
import com.example.qlda.Data.Parser;
import com.example.qlda.Data.Priority;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.StatusType;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.TaskType;
import com.example.qlda.Data.User;
import com.example.qlda.R;
import com.example.qlda.Utils.TimeUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;
import java.util.List;
import java.util.Objects;


public class ItemDetailFragment extends Fragment {
    private static final String ARG_ISSUE = "ARG_ISSUE";
    private Task task;
    private View view;

    BottomSheetDialog curBottomDialog;
    public static ItemDetailFragment newInstance(Task task) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ISSUE, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_ISSUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.issue_details, container, false);

        setupItemDetail(inflater);
        return view;
    }

    private void setupItemDetail(LayoutInflater inflater) {
        // container chứa view của các page mình cần show
        setupBackButton();
        setupSettingButton();
//        showPopupMenu(view);
        setupTaskName();
        setupIssueStatus();
        setupDescription(true);
        setupDetail(true);
        setupFields();
        setupComment();
    }

    private void setupBackButton(){
        // back button implement
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            backToPageListScreen();
        });
    }
    private void setupSettingButton(){
        ImageView imgBtnDotIssue = view.findViewById(R.id.imgBtnDotIssue);
        imgBtnDotIssue.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(getContext()).inflate(R.layout.menu_task_options, null);

            PopupWindow popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true);

            popupView.findViewById(R.id.deleteTask).setOnClickListener(v1 -> {
                popupWindow.dismiss();
                showDeleteTaskDialog(getContext(), task.getTaskName(), () -> {
                    // delete on database
                    boolean removed = Data.getInstance().deleteTaskById(task.getTaskId());
                    if(removed){
                        Toast.makeText(getContext(), "Task đã bị xóa", Toast.LENGTH_SHORT).show();
                        backToPageListScreen();
                    }
                });
            });

            popupView.findViewById(R.id.viewDetails).setOnClickListener(v1 -> {
                popupWindow.dismiss();
                Toast.makeText(getContext(), "Xem chi tiết Task", Toast.LENGTH_SHORT).show();
            });

            popupWindow.showAsDropDown(imgBtnDotIssue, -imgBtnDotIssue.getWidth(), 0);
        });
    }

    private void setupTaskName(){
        TextView taskName = view.findViewById(R.id.edtNameIssue);
        taskName.setText(task.getTaskName());

        ImageButton imgBtnUser = view.findViewById(R.id.imgBtnUser);
        User assign = Data.getInstance().getUserById(task.getAssignedTo());
        imgBtnUser.setBackgroundResource(Parser.getAvatarResource(assign.getAvatarID()));
        imgBtnUser.setOnClickListener(v ->{
            setupChangeUserAssignee(true);
        });

        taskName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    // Ghi log khi nhấn Enter
//                    MyCustomLog.DebugLog("Custom Name", "Completed Edit");
//
//                    // sync với app data để truyền lên server

                    // Ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // Làm mất focus khỏi EditText
                    v.clearFocus();

                    return true;
                }
                return false;
            }
        });
    }
    private void setupIssueStatus(){
        TextView issueStatus = view.findViewById(R.id.txtStatusIssue);
        LinearLayout wrapper = view.findViewById(R.id.wrapper_btnStatus);
        if(Objects.equals(task.getStatus(), StatusType.Todo)){
            wrapper.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.gray_2));
            issueStatus.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.black));
            issueStatus.setText("To Do");
        }
        if(Objects.equals(task.getStatus(),StatusType.InProgress)){
            wrapper.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.blue_1));
            issueStatus.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.blue));
            issueStatus.setText("In Progress");
        }
        if(Objects.equals(task.getStatus(),StatusType.Done)){
            wrapper.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.green_low));
            issueStatus.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.green_high));
            issueStatus.setText("Done");
        }

        wrapper.setOnClickListener(v -> {
            View selectStatusDialog = showBottomSheetDialog(R.layout.bottomdialog_selectstatus);
            // btn1
            LinearLayout todo = selectStatusDialog.findViewById(R.id.btn_todo);
            todo.setOnClickListener(v1-> {
                task.setStatus(StatusType.Todo);
                curBottomDialog.dismiss();
                setupIssueStatus();
            });
            // btn2
            LinearLayout inProgress = selectStatusDialog.findViewById(R.id.btn_inprogress);
            inProgress.setOnClickListener(v1-> {
                task.setStatus(StatusType.InProgress);
                curBottomDialog.dismiss();
                setupIssueStatus();


            });// btn3
            LinearLayout done = selectStatusDialog.findViewById(R.id.btn_done);
            done.setOnClickListener(v1-> {
                task.setStatus(StatusType.Done);
                curBottomDialog.dismiss();
                setupIssueStatus();
            });
        });

    }
    private void setupDescription(boolean isReset){
        Button btnIssue = view.findViewById(R.id.btnDesIssue);
        TextView textDescription = view.findViewById(R.id.textContentDescription);
        textDescription.setText(task.getDescription());

        if(isReset){
            textDescription.setVisibility(View.GONE);

            btnIssue.setOnClickListener(v -> {
                if(textDescription.getVisibility() == View.VISIBLE){
                    textDescription.setVisibility(View.GONE);
                }
                else if(textDescription.getVisibility() == View.GONE){

                    textDescription.setVisibility(View.VISIBLE);
                }
            });


            textDescription.setOnClickListener(v->{
                View editView = showBottomSheetDialog(R.layout.bottomdialog_edittext);

                EditText editText = editView.findViewById(R.id.text_editText);
                editText.setText(task.getDescription());
                editText.requestFocus();

                ImageButton backButtonEditText = editView.findViewById(R.id.backButtonEditText);
                backButtonEditText.setOnClickListener(v11->{
                    curBottomDialog.dismiss();
                });

                TextView imgBtnDoneEditText = editView.findViewById(R.id.imgBtnDoneEditText);
                imgBtnDoneEditText.setOnClickListener(v12->{
                    task.setDescription(editText.getText().toString());
                    setupDescription(false);
                    curBottomDialog.dismiss();
                });
            });
        }
    }
    private void setupDetail(boolean isReset){
        if(isReset){
            Button detail = view.findViewById(R.id.btnDetailIssue);
            TextView textHintDetail = view.findViewById(R.id.textContentDetails);
            textHintDetail.setVisibility(View.VISIBLE);
            LinearLayout contentDetail = view.findViewById(R.id.contentDetails);
            contentDetail.setVisibility(View.GONE);

            detail.setOnClickListener(v -> {
                if(textHintDetail.getVisibility() == View.VISIBLE){
                    textHintDetail.setVisibility(View.GONE);
                    contentDetail.setVisibility(View.VISIBLE);
                }
                else if(textHintDetail.getVisibility() == View.GONE){
                    textHintDetail.setVisibility(View.VISIBLE);
                    contentDetail.setVisibility(View.GONE);
                }
            });
        }

        ImageView imageTask = view.findViewById(R.id.img_task_type);
        imageTask.setBackgroundResource(Parser.getTaskTypeResource(task.getTaskType()));
        TextView nameTask = view.findViewById(R.id.task_typeName);
        nameTask.setText(task.getTaskType().toString());

        LinearLayout taskType = view.findViewById(R.id.btn_tasktype);
        taskType.setOnClickListener(v ->{
//            setupChangeTaskType();
            View changeTaskView = showBottomSheetDialog(R.layout.bottomdialog_changetasktype);

            LinearLayout taskView = changeTaskView.findViewById(R.id.task);
            taskView.setOnClickListener(v11->{
                task.setTaskType(TaskType.Task);
                curBottomDialog.dismiss();
                setupDetail(false);
            });
            LinearLayout bugView = changeTaskView.findViewById(R.id.bug);
            bugView.setOnClickListener(v11->{
                task.setTaskType(TaskType.Bug);
                curBottomDialog.dismiss();
                setupDetail(false);
            });
            LinearLayout storyView = changeTaskView.findViewById(R.id.story);
            storyView.setOnClickListener(v11->{
                task.setTaskType(TaskType.Story);
                curBottomDialog.dismiss();
                setupDetail(false);
            });
        });

        ImageView imagePriority = view.findViewById(R.id.img_priority);
        imagePriority.setBackgroundResource(Parser.getPriorityTypeResource(task.getPriority()));
        TextView namePriority = view.findViewById(R.id.task_priorityName);
        namePriority.setText(task.getPriority().toString());

        LinearLayout priority = view.findViewById(R.id.btn_priority);
        priority.setOnClickListener(v ->{
//            setupChangeTaskType();
            View changeTaskView = showBottomSheetDialog(R.layout.bottomdialog_selectpriority);

            LinearLayout low = changeTaskView.findViewById(R.id.btn_low);
            low.setOnClickListener(v11->{
                task.setPriority(Priority.Low);
                curBottomDialog.dismiss();
                setupDetail(false);
            });
            LinearLayout medium = changeTaskView.findViewById(R.id.btn_medium);
            medium.setOnClickListener(v11->{
                task.setPriority(Priority.Medium);
                curBottomDialog.dismiss();
                setupDetail(false);
            });
            LinearLayout high = changeTaskView.findViewById(R.id.btn_high);
            high.setOnClickListener(v11->{
                task.setPriority(Priority.High);
                curBottomDialog.dismiss();
                setupDetail(false);
            });
        });


        User assign = Data.getInstance().getUserById(task.getAssignedTo());
        ImageView avatar_assignee = view.findViewById(R.id.avatar_assignee);
        TextView name_assignee = view.findViewById(R.id.name_assignee);
        avatar_assignee.setBackgroundResource(Parser.getAvatarResource(assign.getAvatarID()));
        name_assignee.setText(assign.getFullName());

        LinearLayout assignee = view.findViewById(R.id.btn_assignee);
        assignee.setOnClickListener(v ->{
            setupChangeUserAssignee(true);
        });

        User reporter = Data.getInstance().getUserById(task.getReporter());
        ImageView avatar_reporter = view.findViewById(R.id.avatar_reporter);
        avatar_reporter.setBackgroundResource(Parser.getAvatarResource(reporter.getUserId()));
        TextView name_reporter = view.findViewById(R.id.name_reporter);
        name_reporter.setText(reporter.getFullName());

        LinearLayout reporterBtn = view.findViewById(R.id.btn_reporter);
        reporterBtn.setOnClickListener(v ->{
            setupChangeUserAssignee(false);
        });

    }
    private void setupFields(){
        Button btnField = view.findViewById(R.id.btnFieldIssue);
        TextView textHintField = view.findViewById(R.id.textContentFields);
        textHintField.setVisibility(View.VISIBLE);
        LinearLayout contentFields = view.findViewById(R.id.contentFields);
        contentFields.setVisibility(View.GONE);

        btnField.setOnClickListener(v -> {
            if(textHintField.getVisibility() == View.VISIBLE){
                textHintField.setVisibility(View.GONE);
                contentFields.setVisibility(View.VISIBLE);
            }
            else if(textHintField.getVisibility() == View.GONE){
                textHintField.setVisibility(View.VISIBLE);
                contentFields.setVisibility(View.GONE);
            }
        });

        Project project = Data.getInstance().getProjectById(task.getProjectId());
        ImageView avtProject = view.findViewById(R.id.avt_project);
        avtProject.setBackgroundResource(Parser.getAvatarResource(project.getAvatarID()));
        TextView projectName = view.findViewById(R.id.projectName);
        projectName.setText(project.getProjectName());

        TextView created = view.findViewById(R.id.text_created);
        created.setText(task.getUpdatedDate());

        TextView updated = view.findViewById(R.id.text_updated);
        updated.setText(task.getUpdatedDate());
    }

    private void setupComment(){
        List<Comment> comments = Data.getInstance().getCommentsByTaskId(task.getTaskId());
        LinearLayout listComment = view.findViewById(R.id.list_comment);

        for (Comment c : comments){
            View commentTemplate = getLayoutInflater().inflate(R.layout.item_comment, (ViewGroup) view, false);

            commentTemplate.setPadding(20,0,0,20);
            TextView nameText = commentTemplate.findViewById(R.id.text_name);
            User user = Data.getInstance().getUserById(c.getUserId());
            nameText.setText(user.getFullName());

            ImageView img_avatar = commentTemplate.findViewById(R.id.img_avatar);
            img_avatar.setBackgroundResource(Parser.getAvatarResource(user.getAvatarID()));

            TextView contentText = commentTemplate.findViewById(R.id.text_content);
            contentText.setText(c.getContent());

            // show time comment
            TextView text_time = commentTemplate.findViewById(R.id.text_time);
            MyCustomLog.DebugLog("ABCDHJK",c.getCreatedAt());

            text_time.setText(TimeUtils.timeAgoLocalDate(c.getCreatedAt()));
            // xoa cmt

            // edit

            // copy

            listComment.addView(commentTemplate,0);
        }

        LinearLayout bottom_edit_comment = view.findViewById(R.id.bottom_edit_comment);
        EditText edit_comment = bottom_edit_comment.findViewById(R.id.edit_comment);
        ImageView btn_send = bottom_edit_comment.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(v->{
            // add new comment vao database
            Comment new_cmt= Data.getInstance().createComment(task.getTaskId(), Data.currentUser.getUserId(), edit_comment.getText().toString(), TimeUtils.getCurrentTimeFormatted());

            View commentTemplate = getLayoutInflater().inflate(R.layout.item_comment, (ViewGroup) view, false);

            commentTemplate.setPadding(20,0,0,5);
            TextView nameText = commentTemplate.findViewById(R.id.text_name);
            nameText.setText(Data.currentUser.getFullName());

            ImageView img_avatar = commentTemplate.findViewById(R.id.img_avatar);
            img_avatar.setBackgroundResource(Parser.getAvatarResource(Data.currentUser.getAvatarID()));

            TextView contentText = commentTemplate.findViewById(R.id.text_content);
            contentText.setText(new_cmt.getContent());

            // show time comment
            TextView text_time = commentTemplate.findViewById(R.id.text_time);
            text_time.setText(TimeUtils.timeAgoLocalDate(new_cmt.getCreatedAt()));

            listComment.addView(commentTemplate);

            edit_comment.setText("");
            edit_comment.clearFocus();

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });


    }

    private void setupChangeUserAssignee(boolean isAssign){
        View assign = showBottomSheetDialog(R.layout.bottomdialog_assignee);
        // search people

        // show selected
        LinearLayout selected = assign.findViewById(R.id.selected);
        ImageView avt_selected = selected.findViewById(R.id.avt_selected);
        TextView username_selected = selected.findViewById(R.id.username_selected);

        // find a list user in this project
        List<User> users = Data.getInstance().getUsersByProjectId(task.getProjectId());
        User cur = Data.currentUser;
        User selectedUser;

        if(isAssign){
            selectedUser = Data.getInstance().getUserById(task.getAssignedTo());
        }
        else{
            selectedUser = Data.getInstance().getUserById(task.getReporter());
        }

        users.removeIf(user -> user.getUserId() == selectedUser.getUserId());
        // add them unAssignee
        users.add(0,new User(0,"Un Assign","unassign@gmail.com","",7,(new Date()).toString()));

        avt_selected.setBackgroundResource(Parser.getAvatarResource(selectedUser.getAvatarID()));
        if(selectedUser.getUserId() == 0){
            username_selected.setText("Un Assign");
        }
        else if(cur.getUserId() == selectedUser.getUserId()){
            username_selected.setText("Me");
        }
        else{
            username_selected.setText(selectedUser.getFullName());
        }

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
                setupTaskName();
                setupDetail(false);
            });

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) userBtn.getLayoutParams();
            if (params != null) {
                params.setMargins(0, 8, 0, 8);
                userBtn.setLayoutParams(params);
            }
        }
    }

    private void setupChangeTaskType(){
    }

    private void backToPageListScreen(){
        if (getActivity() != null) {
//            Bundle args = new Bundle();
//            args.putSerializable(ARG_ISSUE, task);
//            getParentFragmentManager().setFragmentResult("ISSUE", args);
            getParentFragmentManager().popBackStack();
        }
    }

    private View showBottomSheetDialog(@LayoutRes int resource) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        curBottomDialog = bottomSheetDialog;

        View bottomSheetView = getLayoutInflater().inflate(resource, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Hiển thị Bottom Sheet
        bottomSheetDialog.show();
        return bottomSheetView;
    }

    private void showDeleteTaskDialog(Context context, String taskName, Runnable onDeleteConfirmed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa Task");
        builder.setMessage("Bạn có chắc chắn muốn xóa task \"" + taskName + "\" không?");

        // Nút Xác nhận
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            if (onDeleteConfirmed != null) {
                onDeleteConfirmed.run();
            }
            dialog.dismiss();
        });

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // choose image and save to database
    private static final int PICK_IMAGE_REQUEST = 1;
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                setUserAvatar(imageUri);
            }
        }
    }

    private void setUserAvatar(Uri imageUri) {
        // Thay đổi avatar trên giao diện người dùng
//        ImageView avatarImageView = view.findViewById(R.id.image_uri);
//        avatarImageView.setImageURI(imageUri);
    }

}
