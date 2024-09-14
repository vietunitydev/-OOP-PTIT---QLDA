package com.example.qlda.home;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlda.R;
import com.google.common.collect.Tables;

import java.util.Date;

public class WorkSpaceFragment extends Fragment {

    private static final String ARG_TABLE = "arg_table";
    WorkListAdapter adapter;
    TableData table;

    public static WorkSpaceFragment newInstance(TableData table) {
        WorkSpaceFragment fragment = new WorkSpaceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TABLE, table);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            table = (TableData) getArguments().getSerializable(ARG_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_workspace, container, false);

        table = AppData.getTableById(table.getId());

        EditText edittext_wspaceName = view.findViewById(R.id.edittext_workspaceName);
        if (table != null) {
            edittext_wspaceName.setText(table.getTitle());
            setupViewPager(inflater, view);
        }
        edittext_wspaceName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Ghi log khi nhấn Enter
                    MyCustomLog.DebugLog("Custom Name", "Completed Edit");

                    // sync với app data để truyền lên server
                    table.setTitle(String.valueOf(edittext_wspaceName.getText()));
//                    AppData.UpdateTable(table);
//                    AppData.uploadDataToServerStatic();

                    // Ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // Làm mất focus khỏi EditText
                    v.clearFocus();

                    // Trả về true để chỉ ra rằng sự kiện đã được xử lý
                    return true;
                }
                // Trả về false nếu sự kiện không được xử lý
                return false;
            }
        });


        return view;
    }

    private void setupViewPager(LayoutInflater inflater, View view) {
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        adapter = new WorkListAdapter(inflater, table.getWorkListPages(), this::showItemDetailFragment);
        viewPager.setAdapter(adapter);

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            backToHomeScreen();
        });

        // disable by code
        LinearLayout popup = view.findViewById(R.id.table_setting_popup);
        WorkListSettingPopup settingPopup = new WorkListSettingPopup(popup,this);

        Button edit_pencil = view.findViewById((R.id.btn_pencil_edit));
        edit_pencil.setOnClickListener(v -> settingPopup.showUI());



        Button edit_plus = view.findViewById((R.id.btn_plus_edit));
        edit_plus.setOnClickListener(v -> {
            FireStoreHelper fs = new FireStoreHelper();
            // create new page
            WorkListPageData wlp = new WorkListPageData("table-id-" + fs.getNewIDTable(),table.getId(),"New Page", new Date());
            MyCustomLog.DebugLog("LOG PAGE Create : ", AppData.convertToJson(wlp));
            int index = adapter.addWorkListPage(wlp);
            viewPager.setCurrentItem(index, true);

        });
    }

    private void showItemDetailFragment(WorkListPageData parent, ElementData e, WorkListAdapter.ListWorkHolder holder) {
        ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(parent, e);
        contentFragment.setWorkListAdapterParent(holder);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, contentFragment)
                .addToBackStack(null)
                .commit();
    }

    public void backToHomeScreen(){
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).goBackToPreviousFragment();
        }
    }

    public void removeThisTable(){
        AppData.deleteTable(table.getId());
    }
}