package com.dodoiot.lockapp.net.thread;

import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/26.
 */
public class UploadFileThread extends Thread {

    String url;
    Map<String, String> params;
    Map<String, File> files;
    final IResponseParser callBack;

    public UploadFileThread(String url, Map<String, String> params, Map<String, File> files,
                            final IResponseParser callBack) {
        // TODO Auto-generated constructor stub
        this.url = url;
        this.params = params;
        this.files = files;
        this.callBack = callBack;

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {

            RequestManager.connectionFilePostMsg(url, params, files, callBack);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
