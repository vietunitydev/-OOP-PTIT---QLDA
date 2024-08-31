package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        TextView titleTextView = view.findViewById(R.id.itemTextView);
        if (table != null) {
            titleTextView.setText(table.getTableName());
            setupViewPager(inflater, view);
        }

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

    private void showItemDetailFragment(Element e) {
        ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(e);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, contentFragment)
                .addToBackStack(null)
                .commit();
    }
}