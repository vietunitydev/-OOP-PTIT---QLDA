package com.example.qlda.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlda.Data.AppData;
import com.example.qlda.Data.ElementData;
import com.example.qlda.Data.TableData;
import com.example.qlda.Data.WorkListPageData;
import com.example.qlda.R;

import java.util.Date;

public class WorkSpaceFragment extends Fragment {

    private static final String ARG_TABLE = "arg_table";
    WorkListAdapter adapter;
    TableData table;

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout pagesContainer;
    private GestureDetector gestureDetector;
    private int currentPage = 0;

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

//        table = AppData.getTableById(table.getId());

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        pagesContainer = view.findViewById(R.id.pagesContainer);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX < -1000) { // Vuốt nhanh sang phải
                    scrollToNextPage();
                } else if (velocityX > 1000) { // Vuốt nhanh sang trái
                    scrollToPreviousPage();
                }
                return true;
            }
        });

        horizontalScrollView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));


//        // Tìm các phần tử giao diện
//        TextView draggableElement1 = view.findViewById(R.id.draggableElement1);
//        TextView dropZone2 = view.findViewById(R.id.dropZone2);
//
//        // Setup Drag and Drop cho phần tử
//        setupDragAndDrop(draggableElement1, dropZone2);

        return view;
    }

    private void setupDragAndDrop(View draggableElement, View dropZone) {
        // Xử lý kéo phần tử
        draggableElement.setOnLongClickListener(v -> {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });

        // Xử lý thả phần tử
        dropZone.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    return true;

                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    ViewGroup oldParent = (ViewGroup) draggedView.getParent();
                    oldParent.removeView(draggedView);

                    FrameLayout newParent = (FrameLayout) v;
                    newParent.addView(draggedView);

                    draggedView.setX(event.getX() - draggedView.getWidth() / 3);
                    draggedView.setY(event.getY() - draggedView.getHeight() / 3);

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    return true;

                default:
                    return false;
            }
        });
    }

    private void scrollToNextPage() {
        int totalPages = pagesContainer.getChildCount();
        if (currentPage < totalPages - 1) {
            currentPage++;
            smoothScrollToPage(currentPage);
        }
    }

    private void scrollToPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            smoothScrollToPage(currentPage);
        }
    }

    private void smoothScrollToPage(int page) {
//        int scrollX = page * horizontalScrollView.getWidth();
        int scrollX = page * 250;
        horizontalScrollView.smoothScrollTo(scrollX, 0);
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