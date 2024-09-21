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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.qlda.R;


public class ItemDetailFragment extends Fragment {

    private static final String ARG_Element = "arg_element";
    private static final String ARG_Parent = "arg_parent";

    private ElementData element;
    private WorkListPageData parent;

    private WorkListAdapter.ListWorkHolder workSpaceFragment;

    private LayoutInflater inflaterOwner;
    private View view;
    public static ItemDetailFragment newInstance(WorkListPageData parent, ElementData e) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_Element, e);
        args.putSerializable(ARG_Parent, parent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            element = (ElementData) getArguments().getSerializable(ARG_Element);
            parent = (WorkListPageData) getArguments().getSerializable(ARG_Parent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflaterOwner = inflater;
        this.view = inflater.inflate(R.layout.screen_detail_worklist_element, container, false);

        setupItemDetail(inflater);
        return view;
    }

    private void setupItemDetail(LayoutInflater inflater) {
        // container chứa view của các page mình cần show

        TextView name = view.findViewById(R.id.edittext_element);
        name.setText(element.getTitle());

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Ghi log khi nhấn Enter
                    MyCustomLog.DebugLog("Custom Name", "Completed Edit");

                    // sync với app data để truyền lên server
                    element.setTitle(String.valueOf(name.getText()));
//                    AppData.UpdateElement(element);
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

        Button deleteButton = view.findViewById(R.id.btn_bin);
        deleteButton.setOnClickListener(v -> {
            deleteMe();
        });

        TextView parentName = view.findViewById(R.id.text_parent_name);
        parentName.setText(parent.getTitle());

        TextView des = view.findViewById(R.id.text_describe);
        des.setText(element.getDescription());

        TextView startDay = view.findViewById(R.id.text_start_date);
        startDay.setText("" + element.getCreatedAt());

        TextView endDay = view.findViewById(R.id.text_end_date);
        endDay.setText("" + element.getUpdatedAt());

        // back button implement
        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            backToPageListScreen();
        });

        TextView commentText = view.findViewById(R.id.edit_comment);
        commentText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Ghi log khi nhấn Enter
                    // MyCustomLog.DebugLog("Custom Name", "Completed Edit");

                    // get information user
                    addNewComment("Name", commentText.getText().toString());

                    commentText.setText("");

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


    }

    private void deleteMe(){
        AppData.deleteElement(element.getTableID(),element.getWorkListPageID(),element.getId());
        backToPageListScreen();
        workSpaceFragment.showUI();
    }

    private void addNewComment(String name, String content){
        LinearLayout listComment = view.findViewById(R.id.list_comment);
        View commentTemplate = inflaterOwner.inflate(R.layout.item_comment, (ViewGroup) view, false);

        TextView nameText = commentTemplate.findViewById(R.id.text_name);
        nameText.setText(name);

        TextView contentText = commentTemplate.findViewById(R.id.text_content);
        contentText.setText(content);

        listComment.addView(commentTemplate);
    }

    private void backToPageListScreen(){
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).goBackToPreviousFragment();
        }
    }

    public void setWorkListAdapterParent(WorkListAdapter.ListWorkHolder wp){
        this.workSpaceFragment = wp;
    }
}
