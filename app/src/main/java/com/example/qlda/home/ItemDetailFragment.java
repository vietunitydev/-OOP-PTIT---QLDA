package com.example.qlda.home;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.qlda.Data.Comment;
import com.example.qlda.Data.Data;
import com.example.qlda.Data.Parser;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.User;
import com.example.qlda.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Objects;


public class ItemDetailFragment extends Fragment {
    private static final String ARG_ISSUE = "ARG_ISSUE";
    private Task task;
    private LayoutInflater inflaterOwner;
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
        this.inflaterOwner = inflater;
        this.view = inflater.inflate(R.layout.issue_details, container, false);

        setupItemDetail(inflater);
        return view;
    }

    private void setupItemDetail(LayoutInflater inflater) {
        // container chứa view của các page mình cần show
        setupBackButton();
        setupTaskName();
        setupIssueStatus();
        setupDescription();
        setupDetail();
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

    private void setupTaskName(){
        TextView taskName = view.findViewById(R.id.edtNameIssue);
        taskName.setText(task.getTaskName());

        ImageButton imgBtnUser = view.findViewById(R.id.imgBtnUser);
        imgBtnUser.setOnClickListener(v ->{
            setupHandleAssignee();
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
        if(Objects.equals(task.getStatus(),"Todo")){
            wrapper.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.gray_2));
            issueStatus.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.black));
            issueStatus.setText("TO DO");
        }
        if(Objects.equals(task.getStatus(),"InProgress")){
            wrapper.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.blue_1));
            issueStatus.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.blue));
            issueStatus.setText("In Progress");
        }
        if(Objects.equals(task.getStatus(),"Done")){
            wrapper.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.green_low));
            issueStatus.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.green_high));
            issueStatus.setText("DONE");
        }

        Button btnStatus = view.findViewById(R.id.btnStatusIssue);
        btnStatus.setOnClickListener(v -> {
            showBottomSheetDialog(R.layout.bottomdialog_selectstatus);
        });

    }
    private void setupDescription(){
        Button btnIssue = view.findViewById(R.id.btnDesIssue);
        TextView textDescription = view.findViewById(R.id.textContentDescription);
        textDescription.setVisibility(View.GONE);
        btnIssue.setOnClickListener(v -> {
            if(textDescription.getVisibility() == View.VISIBLE){
                textDescription.setVisibility(View.GONE);
            }
            else if(textDescription.getVisibility() == View.GONE){

                textDescription.setVisibility(View.VISIBLE);
                textDescription.setText(task.getDescription());
            }
        });
    }
    private void setupDetail(){
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

        ImageView imageTask = view.findViewById(R.id.img_task_type);
        imageTask.setBackgroundResource(Parser.getTaskTypeResource(task.getTaskType()));
        TextView nameTask = view.findViewById(R.id.task_typeName);
        nameTask.setText(task.getTaskType());

        LinearLayout taskType = view.findViewById(R.id.btn_tasktype);
        taskType.setOnClickListener(v ->{
            setupChangeTaskType();
        });

        User assign = Data.getInstance().getUserById(task.getAssignedTo());
        ImageView avatar_assignee = view.findViewById(R.id.avatar_assignee);
        avatar_assignee.setBackgroundResource(Parser.getAvatarResource(assign.getUserId()));
        TextView name_assignee = view.findViewById(R.id.name_assignee);
        name_assignee.setText(assign.getFullName());

        User reporter = Data.getInstance().getUserById(task.getReporter());
        ImageView avatar_reporter = view.findViewById(R.id.avatar_reporter);
        avatar_reporter.setBackgroundResource(Parser.getAvatarResource(reporter.getUserId()));
        TextView name_reporter = view.findViewById(R.id.name_reporter);
        name_reporter.setText(reporter.getFullName());

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
        created.setText(task.getDueDate());

        TextView updated = view.findViewById(R.id.text_updated);
        updated.setText(task.getDueDate());
    }

    private void setupComment(){
        List<Comment> comments = Data.getInstance().getCommentsByTaskId(task.getTaskId());

        for (Comment c : comments){
            LinearLayout listComment = view.findViewById(R.id.list_comment);
            View commentTemplate = inflaterOwner.inflate(R.layout.item_comment, (ViewGroup) view, false);

            commentTemplate.setPadding(20,0,0,5);
            TextView nameText = commentTemplate.findViewById(R.id.text_name);
            User user = Data.getInstance().getUserById(c.getUserId());
            nameText.setText(user.getFullName());

            TextView contentText = commentTemplate.findViewById(R.id.text_content);
            contentText.setText(c.getContent());

            listComment.addView(commentTemplate,0);
        }

    }

    private void setupHandleAssignee(){
        View assign = showBottomSheetDialog(R.layout.bottomdialog_assignee);
    }

    private void setupChangeTaskType(){
        View assign = showBottomSheetDialog(R.layout.bottomdialog_changetasktype);
    }

    private void backToPageListScreen(){
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).goBackToPreviousFragment();
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

    private void deleteMe(){
//        AppData.deleteElement(element.getTableID(),element.getWorkListPageID(),element.getId());
//        backToPageListScreen();
//        workSpaceFragment.showUI();
    }

    private void addNewComment(String content){
//        LinearLayout listComment = view.findViewById(R.id.list_comment);
//        View commentTemplate = inflaterOwner.inflate(R.layout.item_comment, (ViewGroup) view, false);
//
//        UserData user = AppData.myUserData;
//
//        TextView nameText = commentTemplate.findViewById(R.id.text_name);
//        nameText.setText(user.getDisplayName());
//
//        TextView contentText = commentTemplate.findViewById(R.id.text_content);
//        contentText.setText(content);
//
//        listComment.addView(commentTemplate,0);
//
//        ElementData.Comment newComment = new ElementData.Comment(user.getId(), user.getEmail(), user.getAvatar(), user.getDisplayName(), content, new Date());
//        element.addComments(newComment);

//        MyCustomLog.DebugLog("ADD new comment", AppData.convertToJson(newComment));
//        MyCustomLog.DebugLog("ADD new comment", "element " + AppData.convertToJson(element));

//        AppData.updateElement(element.getTableID(),element.getWorkListPageID(),element);
//        AppData.uploadDataToServerStatic();

    }


    private void showComment(){
//        List<ElementData.Comment> comments = element.getComments();
//
//        for (ElementData.Comment c : comments){
//            LinearLayout listComment = view.findViewById(R.id.list_comment);
//            View commentTemplate = inflaterOwner.inflate(R.layout.item_comment, (ViewGroup) view, false);
//
//            TextView nameText = commentTemplate.findViewById(R.id.text_name);
//            nameText.setText(c.getUserDisplayName());
//
//            TextView contentText = commentTemplate.findViewById(R.id.text_content);
//            contentText.setText(c.getContent());
//
//            listComment.addView(commentTemplate,0);
//        }
    }


}
