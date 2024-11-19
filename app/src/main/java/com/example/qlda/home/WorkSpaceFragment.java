package com.example.qlda.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
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


    LinearLayout wl_content_scroll1 ;
    LinearLayout wl_content_scroll2 ;
    LinearLayout wl_content_scroll3 ;

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

         wl_content_scroll1 = view1.findViewById(R.id.wl_content_scroll);
         wl_content_scroll2 = view2.findViewById(R.id.wl_content_scroll);
         wl_content_scroll3 = view3.findViewById(R.id.wl_content_scroll);


        List<Task> elms = Data.getInstance().getTasksByProjectId(projectID);
        for (int i = 0; i < elms.size(); i++) {
            if(Objects.equals(elms.get(i).getStatus(), "ToDo")){
                CreateElement(wl_content_scroll1, elms.get(i).getTaskName());
            }
            else if(Objects.equals(elms.get(i).getStatus(), "InProgress")){
                CreateElement(wl_content_scroll2, elms.get(i).getTaskName());
            }
            else if(Objects.equals(elms.get(i).getStatus(), "Done")){
                CreateElement(wl_content_scroll3, elms.get(i).getTaskName());
            }
        }

        // Thiết lập Drag and Drop
        setupDragAndDrop(wl_content_scroll1);
        setupDragAndDrop(wl_content_scroll2);
        setupDragAndDrop(wl_content_scroll3);

        return view;
    }

    private void setupDragAndDrop(LinearLayout container) {
        // Tạo placeholder
        View placeholder = new View(getContext());
        placeholder.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                40
        ));
        placeholder.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        placeholder.setVisibility(View.GONE); // Ẩn ban đầu
        container.addView(placeholder);

        container.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DragAndDrop", "Drag started");
                    placeholder.setVisibility(View.VISIBLE); // Hiển thị placeholder
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    float y = event.getY(); // Lấy vị trí Y của con trỏ trong container
                    int index = getInsertIndex(container, y);
                    Log.d("DragAndDrop", "Y: " + y + " index: " + index);
                    movePlaceholder(container, placeholder, index);
                    return true;

                case DragEvent.ACTION_DROP:
                    Log.d("DragAndDrop", "Task dropped into container: " + container.getId());
                    View draggedView = (View) event.getLocalState();
                    ViewGroup oldParent = (ViewGroup) draggedView.getParent();

                    // Xóa task khỏi container cũ
                    oldParent.removeView(draggedView);

                    // Thêm task vào vị trí của placeholder
                    int placeholderIndex = container.indexOfChild(placeholder);
                    container.removeView(placeholder);
                    container.addView(draggedView, placeholderIndex);

                    placeholder.setVisibility(View.GONE); // Ẩn placeholder sau khi thả
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("DragAndDrop", "Drag exited container: " + container.getId());
                    placeholder.setVisibility(View.GONE); // Ẩn placeholder khi ra ngoài
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("DragAndDrop", "Drag ended");
                    placeholder.setVisibility(View.GONE); // Ẩn placeholder khi kết thúc
                    return true;

                default:
                    return false;
            }
        });
    }

    /**
     * Xác định vị trí chèn dựa trên vị trí Y của con trỏ.
     */
    private int getInsertIndex(LinearLayout container, float y) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (y < child.getY() + child.getHeight() / 2) {
                return i; // Chèn trước phần tử này
            }
        }
        return container.getChildCount(); // Nếu con trỏ nằm cuối, chèn ở cuối
    }

    /**
     * Di chuyển placeholder đến vị trí được chỉ định.
     */
    private void movePlaceholder(LinearLayout container, View placeholder, int index) {
        container.removeView(placeholder);
        container.addView(placeholder, index-1);
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

    private void CreateElement(LinearLayout root, String cnt) {
        View element = inflater.inflate(R.layout.task_item, root, false);
//        elements.add(element);
        TextView text = element.findViewById(R.id.item_task_text);
        text.setText(cnt);

        root.addView(element);
        element.setOnTouchListener(new View.OnTouchListener() {
            long LONG_PRESS_TIME = 500;
            Handler handler = new Handler();
            Runnable longPressRunnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // case này run khi có 1 click vào
                        longPressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                                v.startDragAndDrop(null, shadowBuilder, v, 0);
                            }
                        };
                        handler.postDelayed(longPressRunnable, LONG_PRESS_TIME);
                        MyCustomLog.DebugLog("(ON TOUCH EVENT)", "action down");
                        break;

                    case MotionEvent.ACTION_UP:
                        // case này run khi click không đủ lâu
                        handler.removeCallbacks(longPressRunnable);
                        if (event.getEventTime() - event.getDownTime() < LONG_PRESS_TIME) {
                            v.performClick();
                        }

                        ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(new WorkListPageData(), new ElementData());
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, contentFragment)
                                .addToBackStack(null)
                                .commit();

                        MyCustomLog.DebugLog("(ON TOUCH EVENT)", "action up");
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        // case này run khi click đã đủ lâu
                        handler.removeCallbacks(longPressRunnable);
                        MyCustomLog.DebugLog("(ON TOUCH EVENT)", "action cancel");
                        break;
                }
                return true;
            }
        });
    }

    public void backToHomeScreen(){
        if (getActivity() != null) {
            ((HomeActivity) getActivity()).goBackToPreviousFragment();
        }
    }
}