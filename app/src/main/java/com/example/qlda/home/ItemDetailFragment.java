package com.example.qlda.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.qlda.R;

public class ItemDetailFragment extends Fragment {

    private static final String ARG_Element = "arg_element";
    Element element;

    public static ItemDetailFragment newInstance(Element e) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_Element, e);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            element = (Element) getArguments().getSerializable(ARG_Element);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_detail_worklist_element, container, false);

        setupItemDetail(inflater, view);

        return view;
    }

    private void setupItemDetail(LayoutInflater inflater, View view) {

        // container chứa view của các page mình cần show

        // truyển ảnh vào để set content cho 1 worklist adapter
        // bây giờ cần truyền vào số lượng adapter, số element của 1 adapter, content của 1 element

        TextView name = view.findViewById(R.id.Title);
        name.setText(element.getElementName());

        TextView des = view.findViewById(R.id.describe_content);
        des.setText(element.getDescription());

        // back button implement
        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity()).goBackToPreviousFragment();
            }
        });
    }
}
