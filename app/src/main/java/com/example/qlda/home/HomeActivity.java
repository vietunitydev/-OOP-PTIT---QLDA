package com.example.qlda.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.qlda.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout layout;

    private List<TableData> tables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        // Initialize LayoutInflater and LinearLayout
        inflater = LayoutInflater.from(this);
        layout = findViewById(R.id.buttonContainer);

        LinearLayout downLayout = findViewById(R.id.bottom_navigation);
        DownNavigation downNavigation = new DownNavigation(this, downLayout);

        AppData appData = new AppData();

        // fetch Data for app
        appData.fetchData(() -> {
            tables = appData.getTables();

            MyCustomLog.DebugLog("FireBase Store", String.format("Fetched Data Successfully %d", tables.size()));
            for (TableData data : tables) {
                createButton(data);
            }
        });

        Button addBtn = findViewById(R.id.wl_content_btnAdd);
        // Add new button on click
        addBtn.setOnClickListener(v -> {
            MyCustomLog.Toast(this,"Click Add Table Button");
            TableData newTable = new TableData("table-id-2","New Table", "color", new Date());
            tables.add(newTable);
            createButton(newTable);
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
//         wait 2s
//        new Handler().postDelayed(() -> {
//            MyCustomLog.DebugLog("JSON TABLE Waiter",AppData.convertToJson(AppData.getTables()));
//        }, 2000);

        layout.addView(customButton);
    }

    private void UpdateUI(){
        tables = AppData.Tables;
        layout.removeAllViews();
        // ve lai
        for (TableData data : tables) {
            createButton(data);
        }
//        Yêu cầu LinearLayout vẽ lại
//        layout.invalidate();
//        layout.requestLayout();
    }

    public void goBackToPreviousFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            UpdateUI();
            getSupportFragmentManager().popBackStack();
        } else {
            // out activity
            finish();
        }
    }
}

