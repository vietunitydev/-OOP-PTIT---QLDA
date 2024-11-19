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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlda.Data.AppData;
import com.example.qlda.Data.Data;
import com.example.qlda.Data.ElementData;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.TableData;
import com.example.qlda.Data.Task;
import com.example.qlda.Data.WorkListPageData;
import com.example.qlda.R;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WorkSpaceFragment extends Fragment {

    private static final String ARG_TABLE = "arg_table";

    LayoutInflater inflater;

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
//            table = (TableData) getArguments().getSerializable(ARG_TABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.screen_workspace, container, false);

        // need get from passing data
        int projectID = 1;


        Project project = Data.getInstance().getProjectById(projectID);
        TextView edittext_wspaceName = view.findViewById(R.id.edittext_workspaceName);
        if (project != null) {
            edittext_wspaceName.setText(project.getProjectName());
        }

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            backToHomeScreen();
        });

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        pagesContainer = view.findViewById(R.id.pagesContainer);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX < -1000) {
                    scrollToNextPage();
                } else if (velocityX > 1000) {
                    scrollToPreviousPage();
                }
                return true;
            }
        });

        horizontalScrollView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        FrameLayout page1 = view.findViewById(R.id.page1);
        FrameLayout page2 = view.findViewById(R.id.page2);
        FrameLayout page3 = view.findViewById(R.id.page3);

        View view1 = inflater.inflate(R.layout.layout_worklist, null, false);
        TextView textTitle1 = view1.findViewById(R.id.wl_content_worklist_name);
        textTitle1.setText("TODO");
        View view2 = inflater.inflate(R.layout.layout_worklist, null, false);
        TextView textTitle2 = view2.findViewById(R.id.wl_content_worklist_name);
        textTitle2.setText("IN PROGRESS");
        View view3 = inflater.inflate(R.layout.layout_worklist, null, false);
        TextView textTitle3 = view3.findViewById(R.id.wl_content_worklist_name);
        textTitle3.setText("DONE");

        page1.addView(view1);
        page2.addView(view2);
        page3.addView(view3);

        LinearLayout wl_content_scroll1 = view1.findViewById(R.id.wl_content_scroll);
        LinearLayout wl_content_scroll2 = view2.findViewById(R.id.wl_content_scroll);
        LinearLayout wl_content_scroll3 = view3.findViewById(R.id.wl_content_scroll);


        List<Task> elms = Data.getInstance().getTasksByProjectId(projectID);
        for (int i = 0; i < elms.size(); i++) {
            if(Objects.equals(elms.get(i).getStatus(), "ToDo")){
                CreateElement(wl_content_scroll1, i, elms.get(i).getTaskName());
            }
            else if(Objects.equals(elms.get(i).getStatus(), "InProgress")){
                CreateElement(wl_content_scroll2, i, elms.get(i).getTaskName());
            }
            else if(Objects.equals(elms.get(i).getStatus(), "Done")){
                CreateElement(wl_content_scroll3, i, elms.get(i).getTaskName());
            }
        }



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

    private void CreateElement(LinearLayout root, int index, String cnt) {
        FrameLayout element = (FrameLayout) inflater.inflate(R.layout.item_worklist, null, false);
//        elements.add(element);
        TextView text = element.findViewById(R.id.item_drag_text);
        text.setText(cnt);

//        Button btn = element.findViewById(R.id.item_drag_btn);
//        btn.setOnClickListener(v -> {
//            if (onElementClickListener != null) {
//                onElementClickListener.onElementClick(workListPage, workListPage.getElements().get(index), this);
//            }
//        });


        root.addView(element);
    }

    public void backToHomeScreen(){
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).goBackToPreviousFragment();
        }
    }

//    public void removeThisTable(){
//        AppData.deleteTable(table.getId());
//    }
}