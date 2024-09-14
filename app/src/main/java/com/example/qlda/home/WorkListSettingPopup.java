package com.example.qlda.home;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.qlda.R;

public class WorkListSettingPopup {
    private final LinearLayout popup;
    private final WorkSpaceFragment workSpaceFragment;
    public WorkListSettingPopup(View view,WorkSpaceFragment fragment){
        this.popup = view.findViewById(R.id.table_setting_popup);
        this.workSpaceFragment = fragment;
        // default = gone
        setUpUI();
    }

    public void setUpUI(){
        popup.setVisibility(View.GONE);

        Button deleteTable = popup.findViewById(R.id.btn_delete_table);
        deleteTable.setOnClickListener(v -> {
            workSpaceFragment.removeThisTable();
            workSpaceFragment.backToHomeScreen();
        });

    }

    public void showUI(){
        if(popup.getVisibility() == View.VISIBLE){
            popup.setVisibility(View.GONE);
        }
        else if(popup.getVisibility() == View.GONE){
            popup.setVisibility(View.VISIBLE);
        }
    }
}
