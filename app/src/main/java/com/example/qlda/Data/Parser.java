package com.example.qlda.Data;

import com.example.qlda.R;

import com.example.qlda.R;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Parser {

    private static final int[] AVATAR_RESOURCES = {
            R.drawable.avt_5,
            R.drawable.avt_1,
            R.drawable.avt_2,
            R.drawable.avt_3,
            R.drawable.avt_4,
            R.drawable.avt_6,
            R.drawable.avt_7,
            R.drawable.avt_8,
    };

    private static final Map<TaskType, Integer> TASK_TYPE_MAP = new HashMap<>();
    private static final Map<Priority, Integer> PRIORITY_TYPE_MAP = new HashMap<>();
    private static final Map<StatusType, Integer> STATUS_TYPE_MAP = new HashMap<>();

    // Static block để khởi tạo ánh xạ
    static {
        // Map TaskType to Resources
        TASK_TYPE_MAP.put(TaskType.Story, R.drawable.avt_story);
        TASK_TYPE_MAP.put(TaskType.Task, R.drawable.avt_task);
        TASK_TYPE_MAP.put(TaskType.Bug, R.drawable.avt_bug);

        // Map PriorityType to Resources
        PRIORITY_TYPE_MAP.put(Priority.Lowest, R.drawable.avt_high);
        PRIORITY_TYPE_MAP.put(Priority.Low, R.drawable.avt_low);
        PRIORITY_TYPE_MAP.put(Priority.Medium, R.drawable.avt_medium);
        PRIORITY_TYPE_MAP.put(Priority.High, R.drawable.avt_high);
        PRIORITY_TYPE_MAP.put(Priority.Highest, R.drawable.avt_high);

        // Map StatusType to Resources
//        STATUS_TYPE_MAP.put(StatusType.TODO, R.drawable.status_todo);
//        STATUS_TYPE_MAP.put(StatusType.IN_PROGRESS, R.drawable.status_in_progress);
//        STATUS_TYPE_MAP.put(StatusType.DONE, R.drawable.status_done);
//        STATUS_TYPE_MAP.put(StatusType.CANCELED, R.drawable.status_canceled);
    }

    /**
     * Lấy resource ID của ảnh dựa trên ID truyền vào.
     *
     * @param id chỉ số của avatar (0 đến 9)
     * @return resource ID của avatar hoặc -1 nếu id không hợp lệ
     */
    public static int getAvatarResource(int id) {
        if (id >= 0 && id < AVATAR_RESOURCES.length) {
            return AVATAR_RESOURCES[id];
        } else {
            return  AVATAR_RESOURCES[clamp(1,AVATAR_RESOURCES.length-1, id)];
        }
    }

    /**
     * Lấy resource ID dựa trên TaskType.
     *
     * @param taskType kiểu nhiệm vụ
     * @return resource ID hoặc -1 nếu không tìm thấy
     */
    public static int getTaskTypeResource(TaskType taskType) {
        return TASK_TYPE_MAP.getOrDefault(taskType, -1);
    }

    /**
     * Lấy resource ID dựa trên PriorityType.
     *
     * @param priorityType mức độ ưu tiên
     * @return resource ID hoặc -1 nếu không tìm thấy
     */
    public static int getPriorityTypeResource(Priority priorityType) {
        return PRIORITY_TYPE_MAP.getOrDefault(priorityType, -1);
    }

    /**
     * Lấy resource ID dựa trên StatusType.
     *
     * @param statusType trạng thái nhiệm vụ
     * @return resource ID hoặc -1 nếu không tìm thấy
     */
    public static int getStatusTypeResource(StatusType statusType) {
        return STATUS_TYPE_MAP.getOrDefault(statusType, -1);
    }

    public static int clamp(int a, int max, int b) {
        if (b < a) {
            return a;
        }
        if (b > max) {
            return max;
        }
        return b;
    }
}

