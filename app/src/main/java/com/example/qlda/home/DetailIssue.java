package com.example.qlda.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.R;

import com.example.qlda.Data.Task;
import com.example.qlda.Data.Data;

public class DetailIssue extends AppCompatActivity {
    private static Data data = Data.getInstance();
    private static Task task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.issue_details);


        rollBackIssue();
        assignDataIssue();
        deleteIssue();
    }
    private void rollBackIssue(){
        ImageButton imgBtnBackIssues;
        imgBtnBackIssues = (ImageButton) findViewById(R.id.imgBtnBackIssues);

        imgBtnBackIssues.setOnClickListener(v ->{
            finish();
        });
    }
    private void assignDataIssue(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int taskId = bundle.getInt("taskId");

            task = data.getIssueById(taskId);

            //Gán dữ liêuj
            EditText edtNameIssue = (EditText) findViewById(R.id.edtNameIssue);
            edtNameIssue.setText(task.getTaskName());

            TextView txtStatusIssue = (TextView) findViewById(R.id.txtStatusIssue);
            txtStatusIssue.setText(task.getStatus());
        }
    }
    private void deleteIssue(){
        ImageButton imgBtnDotIssue = (ImageButton) findViewById(R.id.imgBtnDotIssue);

        imgBtnDotIssue.setOnClickListener(v ->{
            View popupView = LayoutInflater.from(this).inflate(R.layout.item_popup_menu, null);
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    true
            );

            popupWindow.showAsDropDown(imgBtnDotIssue);

            // Xử lý khi ấn vào nút Delete trong popup
            popupView.findViewById(R.id.btnDeleteIssue).setOnClickListener(t -> {
                popupWindow.dismiss(); // Đóng popup
                showDeleteDialog();   // Hiển thị màn hình xác nhận
            });
        });
    }
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentDialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_dialog_cf_delete, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        // Xử lý nút "Cancel"
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());

        // Xử lý nút "Delete"
        dialogView.findViewById(R.id.btn_confirm_delete).setOnClickListener(v -> {
            alertDialog.dismiss();

            data.deleteIssueById(task.getTaskId());

            // Quay lại màn hình HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        alertDialog.show();
    }
}
