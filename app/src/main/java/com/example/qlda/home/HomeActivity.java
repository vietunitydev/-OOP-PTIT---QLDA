package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.qlda.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private int buttonIdCounter = 5;
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
            createButton(data.getId(), data.getTableName(), getRandomColor());
        }

        Button addBtn = findViewById(R.id.btnAdd);
        // Add new button on click
        addBtn.setOnClickListener(v -> {
            MyCustomLog.Toast(this,"Click Add Table Button");
            createButton(buttonIdCounter++, "New Table", getRandomColor());
        });
    }

    private void createButton(int id, String text, int color) {
        AppCompatButton button = (AppCompatButton) inflater.inflate(R.layout.custom_button_table_workspace, layout, false);
        button.setId(id);
        button.setText(text);
        button.setBackgroundColor(ContextCompat.getColor(this, color));

        button.setOnClickListener(v -> {
            String toastMessage = String.format("Click Button ID: %d, Text: %s", id, text);
            MyCustomLog.Toast(this,toastMessage);
//            setContentView(R.layout.workspace);
//            setupViewPager();
            WorkSpaceFragment contentFragment = WorkSpaceFragment.newInstance(text, color);

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
    private int getRandomColor() {
        int[] colors = {
            R.color.blue,
            R.color.purple,
            R.color.pink,
            R.color.deep_purple
        };
        return colors[new Random().nextInt(colors.length)];
    }
}

