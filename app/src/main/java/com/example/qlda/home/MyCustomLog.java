package com.example.qlda.home;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyCustomLog {
    public static void Toast(Context context,String message) {
        Toast.makeText(context , message, Toast.LENGTH_SHORT).show();
    }
    public static void DebugLog(String tag, String content){
        Log.d(tag,content);
    }
}
