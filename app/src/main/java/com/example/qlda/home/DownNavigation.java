package com.example.qlda.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.qlda.R;


import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.motion.widget.Debug;

public class DownNavigation {

    private final Context context;
    private final LinearLayout layout;

    public DownNavigation(Context context, LinearLayout layout) {
        this.context = context;
        this.layout = layout;
        initializeButtons();
    }

    public void initializeButtons() {
        Button btnTable = layout.findViewById(R.id.btnTable);
        Button btnNotification = layout.findViewById(R.id.btnNotification);
        Button btnAccount = layout.findViewById(R.id.btnAccount);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
