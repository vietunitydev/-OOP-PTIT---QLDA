package com.example.qlda.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlda.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout layout;

    private List<TableData> tables = new ArrayList<>();

    private Button showUserInfo;
    private Button addProject;
    AppData appData = new AppData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        // Initialize LayoutInflater and LinearLayout
        inflater = LayoutInflater.from(this);
        layout = findViewById(R.id.buttonContainer);

        LinearLayout downLayout = findViewById(R.id.bottom_navigation);
        DownNavigation downNavigation = new DownNavigation(this, downLayout);

        fetchUserProject();
        setupCreateProject();
        setupShowUserInfo();

    }

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
        addProject = findViewById(R.id.wl_content_btnAdd);
        // Add new button on click
        addProject.setOnClickListener(v -> {
            FireStoreHelper fs = new FireStoreHelper();
            MyCustomLog.Toast(this,"Click Add Table Button");
            TableData newTable = new TableData("table-id-" + fs.getNewIDTable(),"New Table", "#AAAAAA", new Date());
            WorkListPageData newPage = new WorkListPageData("page-id-" + fs.getNewIDTable(),newTable.getId(),"New Page List",new Date());
            newTable.addWorkListPage(newPage);
            createButton(newTable);

            // add vao APPDATA
            AppData.addTable(newTable);
            MyCustomLog.DebugLog("Debug DATA", AppData.convertToJson(AppData.Tables));

            updateUI();
        });
    }

    private void createButton(TableData table) {
        FrameLayout customButton = (FrameLayout) inflater.inflate(R.layout.button_table, layout, false);

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
        layout.addView(customButton);
    }

    private void setupShowUserInfo(){
        showUserInfo = findViewById(R.id.btn_show_user_info);
        // Add new button on click
        showUserInfo.setOnClickListener(v -> {
            MyCustomLog.Toast(this,"Show user info");
            showBottomSheetDialog();
        });
    }

    private void searchProjectByName(String name){

    }

    private void updateUI(){
        tables = AppData.Tables;
        layout.removeAllViews();
        // ve lai
        for (TableData data : tables) {
            createButton(data);
        }
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

    private void showBottomSheetDialog() {
        // Khởi tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Nạp layout cho BottomSheetDialog
        View bottomSheetView = getLayoutInflater().inflate(R.layout.screen_user, null);
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
    }
}

