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
        Button btnCard = layout.findViewById(R.id.btnCard);
        Button btnSearch = layout.findViewById(R.id.btnSearch);
        Button btnNotification = layout.findViewById(R.id.btnNotification);
        Button btnAccount = layout.findViewById(R.id.btnAccount);

        btnTable.setOnClickListener(v -> showToast("Table button clicked"));
        btnCard.setOnClickListener(v -> showToast("Card button clicked"));
        btnSearch.setOnClickListener(v -> showToast("Search button clicked"));
        btnNotification.setOnClickListener(v -> showToast("Notification button clicked"));
        btnAccount.setOnClickListener(v -> showToast("Account button clicked"));
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
