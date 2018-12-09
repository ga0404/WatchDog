package com.dodoiot.lockapp.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TShow {
    public static String TAG = "dingding";

    public static void showToast(Context context, int id){
        Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

    public static void print(String msg){
        System.out.print(msg);
    }

    public static void logMsg(String msg){
        Log.e(TAG, msg);
    }
}
