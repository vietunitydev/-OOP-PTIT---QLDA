package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.example.qlda.R;

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

        // Create buttons dynamically
        List<ButtonData> buttonsData = Arrays.asList(
                new ButtonData(1, "CTDL", R.color.blue),
                new ButtonData(2, "English", R.color.purple),
                new ButtonData(3, "Meta rush", R.color.pink),
                new ButtonData(4, "Study", R.color.deep_purple)
        );

        for (ButtonData data : buttonsData) {
            createButton(data.getId(), data.getText(), data.getColor());
        }

        Button addBtn = findViewById(R.id.btnAdd);
        // Add new button on click
        addBtn.setOnClickListener(v -> {
            showToast("Click Add Table Button");
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
            showToast(toastMessage);
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


    private void showToast(String message) {
        VLog.Toast(this,message);
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

    private static class ButtonData {
        private final int id;
        private final String text;
        private final int color;

        ButtonData(int id, String text, int color) {
            this.id = id;
            this.text = text;
            this.color = color;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public int getColor() {
            return color;
        }
    }
}

