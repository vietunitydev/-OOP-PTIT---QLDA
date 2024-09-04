package com.example.qlda.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.qlda.R;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout layout;

    List<Table> tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        // Initialize LayoutInflater and LinearLayout
        inflater = LayoutInflater.from(this);
        layout = findViewById(R.id.buttonContainer);

        LinearLayout downLayout = findViewById(R.id.bottom_navigation);
        DownNavigation downNavigation = new DownNavigation(this, downLayout);

        // fetch Data for app
        AppData.FetchData(() -> {
            tables = AppData.getTables();

            MyCustomLog.DebugLog("FireBase Store", String.format("Fetched Data Successfully %d", tables.size()));
            for (Table data : tables) {
                createButton(data);
            }
        });



        Button addBtn = findViewById(R.id.wl_content_btnAdd);
        // Add new button on click
        addBtn.setOnClickListener(v -> {
            MyCustomLog.Toast(this,"Click Add Table Button");
            Table newTable = new Table(1,"New Table", "color");
            tables.add(newTable);
            createButton(newTable);
        });
    }

    private void createButton(Table table) {
        AppCompatButton button = (AppCompatButton) inflater.inflate(R.layout.button_table, layout, false);
        button.setId(table.getId());
        button.setText(table.getTableName());
        button.setBackgroundColor(ContextCompat.getColor(this, table.getRandomColor()));

        // wait 2s
        new Handler().postDelayed(() -> {
            MyCustomLog.DebugLog("JSON TABLE Waiter",AppData.convertToJson(AppData.getTables()));
        }, 2000);

        button.setOnClickListener(v -> {
            WorkSpaceFragment contentFragment = WorkSpaceFragment.newInstance(table);
            MyCustomLog.DebugLog("JSON TABLE",AppData.convertToJson(AppData.getTables()));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, contentFragment)
                    .addToBackStack(null)
                    .commit();
        });

        layout.addView(button);
    }

    public void goBackToPreviousFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // out activity
            finish();
        }
    }
}

