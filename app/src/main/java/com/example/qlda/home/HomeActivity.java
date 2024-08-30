package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.qlda.R;

import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private LinearLayout layout;

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
        AppData appData = new AppData();
        appData.fetchData();

        List<Table> table = appData.getTables();

        for (Table data : table) {
            createButton(data);
        }

        Button addBtn = findViewById(R.id.wl_content_btnAdd);
        // Add new button on click
//        addBtn.setOnClickListener(v -> {
//            MyCustomLog.Toast(this,"Click Add Table Button");
//            createButton(buttonIdCounter++, "New Table", getRandomColor());
//        });
    }

    private void createButton(Table table) {
        AppCompatButton button = (AppCompatButton) inflater.inflate(R.layout.custom_button_table_workspace, layout, false);
        button.setId(table.getId());
        button.setText(table.getTableName());
        button.setBackgroundColor(ContextCompat.getColor(this, table.getRandomColor()));

        button.setOnClickListener(v -> {
            WorkSpaceFragment contentFragment = WorkSpaceFragment.newInstance(table);

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

