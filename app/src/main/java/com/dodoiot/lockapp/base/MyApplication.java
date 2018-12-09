package com.dodoiot.lockapp.base;

import android.app.Application;

import com.dodoiot.lockapp.net.RequestManager;

import io.javac.ManyBlue.ManyBlue;

public class MyApplication extends Application {
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    @Override
    public void onCreate() {
        super.onCreate();

        Gloal.onAppStart(getApplicationContext());
        RequestManager.init(this);
//        parseManifests();
//        if (handler == null) {
//            handler = new MessageHandler();
//        }
        ManyBlue.blueStartService(this);

    }


    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ManyBlue.blueStopService(this);
    }
}
