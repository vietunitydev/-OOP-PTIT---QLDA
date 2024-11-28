package com.example.qlda.home;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.qlda.Data.Data;
import com.example.qlda.Data.Parser;
import com.example.qlda.Data.Project;
import com.example.qlda.Data.StatusType;
import com.example.qlda.Data.Task;
import com.example.qlda.R;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class WorkSpaceFragment extends Fragment {

    private static final String ARG_Project = "arg_project";

    LayoutInflater inflater;

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout pagesContainer;
    private GestureDetector gestureDetector;
    private int currentPage = 0;
    LinearLayout wl_content_scroll1 ;
    LinearLayout wl_content_scroll2 ;
    LinearLayout wl_content_scroll3 ;

    Project project;

    ImageView avtarUserSetting;
    Uri currentUri;

    public static WorkSpaceFragment newInstance(Project project) {
        WorkSpaceFragment fragment = new WorkSpaceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_Project, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = (Project) getArguments().getSerializable(ARG_Project);
        }

//        getParentFragmentManager().setFragmentResultListener("ISSUE", this, (requestKey, result) -> {
//            if ("ISSUE".equals(requestKey)) {
//                Task task = (Task) result.getSerializable("ARG_ISSUE");
//                if (task != null) {
//                    // Gọi hàm để cập nhật UI hoặc xử lý logic
////                    updateUI(task);
//                    MyCustomLog.DebugLog("WORKSPACE FRAGMENT", "FRAGMENT on create 2");
//                }
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.screen_workspace, container, false);

        MyCustomLog.DebugLog("WORKSPACE FRAGMENT", "FRAGMENT onCreateView");

        // need get from passing data
        int projectID = project.getProjectId();


        Project project = null;
        try {
            project = Data.getInstance().getProjectById(projectID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TextView edittext_wspaceName = view.findViewById(R.id.edittext_workspaceName);
        if (project != null) {
            edittext_wspaceName.setText(project.getProjectName());
        }

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            backToHomeScreen();
        });

        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        View setting = view.findViewById(R.id.setting_tab);

        // default
        setting.setVisibility(View.GONE);

        Button btnBoard = view.findViewById(R.id.btnBoard);
        Button btnSetting = view.findViewById(R.id.btnSetting);

        btnBoard.setOnClickListener(v ->{
            MyCustomLog.DebugLog("---(Project Fragment)", "btnBoard clicked" + horizontalScrollView.getVisibility());
            if(horizontalScrollView.getVisibility() == View.VISIBLE){

            } else if (horizontalScrollView.getVisibility() == View.GONE) {
                horizontalScrollView.setVisibility(View.VISIBLE);
                setting.setVisibility(View.GONE);}
        });

        btnSetting.setOnClickListener(v ->{
            MyCustomLog.DebugLog("---(Project Fragment)", "btnSetting clicked" + setting.getVisibility());
            if(setting.getVisibility() == View.VISIBLE){

            } else if (setting.getVisibility() == View.GONE) {
                horizontalScrollView.setVisibility(View.GONE);
                setting.setVisibility(View.VISIBLE);
                setupOpenSetting(setting);
            }
        });


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


        List<Task> tasks = null;
        try {
            tasks = Data.getInstance().getTasksByProjectId(projectID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < tasks.size(); i++) {
            if(Objects.equals(tasks.get(i).getStatus(), StatusType.Todo)){
                CreateElement(wl_content_scroll1, tasks.get(i));
            }
            else if(Objects.equals(tasks.get(i).getStatus(), StatusType.InProgress)){
                CreateElement(wl_content_scroll2, tasks.get(i));
            }
            else if(Objects.equals(tasks.get(i).getStatus(), StatusType.Done)){
                CreateElement(wl_content_scroll3, tasks.get(i));
            }
        }

        // Thiết lập Drag and Drop
        setupDragAndDrop(wl_content_scroll1);
        setupDragAndDrop(wl_content_scroll2);
        setupDragAndDrop(wl_content_scroll3);


        return view;
    }

    private void setupOpenSetting(View setting) {
        ImageView avt = setting.findViewById(R.id.iv_avatar);
        avtarUserSetting = avt;
        avt.setBackgroundResource(Parser.getAvatarResource(Data.currentUser.getAvatarID()));

        TextView projectName = setting.findViewById(R.id.tv_project_name);
        projectName.setText(project.getProjectName());

        TextView created = setting.findViewById(R.id.tv_project_created);
        created.setText(project.getStartDate());

        TextView changeAvt = setting.findViewById(R.id.tv_change_avatar);
        changeAvt.setOnClickListener(v->{
            openImagePicker();
        });

        Button moveToTrash = setting.findViewById(R.id.btn_move_to_trash);
        moveToTrash.setOnClickListener(v->{
            moveToTrash(getContext(), ()->{
               // xoa project nay
               backToHomeScreen();
            });
        });
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

    private void CreateElement(LinearLayout root, Task task) {
        View element = inflater.inflate(R.layout.task_item, root, false);
//        elements.add(element);
        TextView text = element.findViewById(R.id.item_task_text);
        text.setText(task.getTaskName());

        TextView text2 = element.findViewById(R.id.task_project_name);
        text2.setText(project.getProjectName());

        ImageView img_task = element.findViewById(R.id.task_type);
        img_task.setBackgroundResource(Parser.getTaskTypeResource(task.getTaskType()));

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

                        ItemDetailFragment contentFragment = ItemDetailFragment.newInstance(task);
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

    // choose image and save to database
    private static final int PICK_IMAGE_REQUEST = 1;
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                currentUri = data.getData();
                avtarUserSetting.setImageURI(currentUri);
            }
        }
    }

    private void moveToTrash(Context context, Runnable actions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc muốn xoá project này không ?");

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (actions != null) {
                actions.run();
            }
            dialog.dismiss();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}