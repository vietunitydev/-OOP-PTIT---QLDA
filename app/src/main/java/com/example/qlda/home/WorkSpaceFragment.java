package com.example.qlda.home;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlda.R;

public class WorkSpaceFragment extends Fragment {

    private static final String ARG_TABLE = "arg_table";
    Table table;

    public static WorkSpaceFragment newInstance(Table table) {
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
            table = (Table) getArguments().getSerializable(ARG_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_workspace, container, false);

        EditText edittext_wspaceName = view.findViewById(R.id.edittext_workspaceName);
        if (table != null) {
            edittext_wspaceName.setText(table.getTableName());
            setupViewPager(inflater, view);
        }
        edittext_wspaceName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Ghi log khi nhấn Enter
                    MyCustomLog.DebugLog("Custom Name", "Completed Edit");

                    // sync với app data để truyền lên server

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
        WorkListAdapter adapter = new WorkListAdapter(inflater, table.getWorkListPages(), this::showItemDetailFragment);
        viewPager.setAdapter(adapter);

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity()).goBackToPreviousFragment();
            }
        });
    }

    private void showItemDetailFragment(WorkListPage parent, Element e) {
        ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(parent, e);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, contentFragment)
                .addToBackStack(null)
                .commit();
    }
}