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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.qlda.Data.Task;
import com.example.qlda.R;

import java.util.Objects;


public class ItemDetailFragment extends Fragment {

    private static final String ARG_ISSUE = "ARG_ISSUE";

    private Task task;

    private WorkListAdapter.ListWorkHolder workSpaceFragment;

    private LayoutInflater inflaterOwner;
    private View view;
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

        TextView taskName = view.findViewById(R.id.edtNameIssue);
        taskName.setText(task.getTaskName());

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

        // back button implement
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            backToPageListScreen();
        });

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
//
//        TextView parentName = view.findViewById(R.id.text_parent_name);
//        parentName.setText(parent.getTitle());
//
//        TextView des = view.findViewById(R.id.text_describe);
//        des.setText(element.getDescription());
//
//        TextView startDay = view.findViewById(R.id.text_start_date);
//        startDay.setText("" + element.getCreatedAt());
//
//        TextView endDay = view.findViewById(R.id.text_end_date);
//        endDay.setText("" + element.getUpdatedAt());
//


//        TextView commentText = view.findViewById(R.id.edit_comment);
//        commentText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    // Ghi log khi nhấn Enter
//                    // MyCustomLog.DebugLog("Custom Name", "Completed Edit");
//
//                    // get information user
//                    addNewComment(commentText.getText().toString());
//
//                    commentText.setText("");
//
//                    // Ẩn bàn phím ảo
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                    // Làm mất focus khỏi EditText
//                    v.clearFocus();
//
//                    // Trả về true để chỉ ra rằng sự kiện đã được xử lý
//                    return true;
//                }
//                // Trả về false nếu sự kiện không được xử lý
//                return false;
//            }
//        });
//
//        showComment();
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

    private void backToPageListScreen(){
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).goBackToPreviousFragment();
        }
    }

    public void setWorkListAdapterParent(WorkListAdapter.ListWorkHolder wp){
        this.workSpaceFragment = wp;
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
