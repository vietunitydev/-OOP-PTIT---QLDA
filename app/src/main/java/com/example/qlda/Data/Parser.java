package com.example.qlda.Data;

import com.example.qlda.R;

public class Parser {
    private static final int[] AVATAR_RESOURCES = {
            R.drawable.avt_1,
            R.drawable.avt_2,
            R.drawable.avt_3,
            R.drawable.avt_4,
            R.drawable.avt_5,
            R.drawable.avt_6,
            R.drawable.avt_7,
            R.drawable.avt_8,
    };

    /**
     * Lấy resource ID của ảnh dựa trên id truyền vào.
     *
     * @param id chỉ số của avatar (0 đến 9)
     * @return resource ID của avatar hoặc -1 nếu id không hợp lệ
     */
    public static int getAvatarResource(int id) {
        if (id >= 0 && id < AVATAR_RESOURCES.length) {
            return AVATAR_RESOURCES[id];
        } else {
            return -1;
        }
    }


}
