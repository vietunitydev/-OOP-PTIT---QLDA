package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlda.R;

import java.util.Arrays;
import java.util.List;

public class WorkSpaceFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_COLOR = "color";

    private String title;
    private int color;

    public static WorkSpaceFragment newInstance(String title, int color) {
        WorkSpaceFragment fragment = new WorkSpaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.workspace, container, false);

        TextView titleTextView = view.findViewById(R.id.itemTextView);
        titleTextView.setText(title);
        setupViewPager(view);
        return view;
    }

    private void setupViewPager(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        List<Integer> imageList = Arrays.asList(R.drawable.ic_bell, R.drawable.ic_card, R.drawable.ic_table);
        ImagePagerAdapter adapter = new ImagePagerAdapter(imageList);
        viewPager.setAdapter(adapter);

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity()).goBackToPreviousFragment();
            }
        });
    }
}