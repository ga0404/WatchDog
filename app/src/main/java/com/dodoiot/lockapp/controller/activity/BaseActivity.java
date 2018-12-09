package com.dodoiot.lockapp.controller.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.view.ProgressDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.manager.EventManager;

public abstract  class BaseActivity extends AppCompatActivity {

    public int layoutId;
    protected TextView tvTitle;
    protected boolean isFull = true;
    protected Button leftBtn,rightBtn;
    protected ProgressDialog mTipDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayoutId();
        if (layoutId == 0)
            return;
        if (!isFull) {
            this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        }else{
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(layoutId);
        ButterKnife.inject(this);
//        if (!isFull){
//            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                    R.layout.layout_title);
//            tvTitle = (TextView) findViewById(R.id.tvtitle);
//            leftBtn = (Button) findViewById(R.id.btnleft);
//            rightBtn = (Button) findViewById(R.id.btnright);
//        }
        initView();
        //initListener();
    }



    protected abstract void initLayoutId();

    protected abstract void initView();
//
//    protected abstract void initListener();




    protected void setTitleId(int titleId) {
        if (tvTitle != null)
            tvTitle.setText(titleId);
    }

    public void hideInputPanel(View v) {
        if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
            if (null == v) v = getCurrentFocus();
            if (null == v) return;
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示错误提示，并获取焦点
     * @param textInputLayout
     * @param error
     */
    protected void showError(TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    protected void mayRequestLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //判断是否跟用户做一个说明
                Toast.makeText(this,R.string.ble_need_location, Toast.LENGTH_LONG).show();
            }
        }

//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                //判断是否需要 向用户解释，为什么要申请该权限
//                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
//                    Toast.makeText(this,R.string.ble_need_location, Toast.LENGTH_LONG).show();
//
//                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
//                return;
//            }else{
//
//            }
//        } else {
//
//        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventManager.getLibraryEvent().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventManager.getLibraryEvent().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyMessage notifyMessage) {
        if (this instanceof BaseNotifyListener)
            ManyBlue.dealtListener((BaseNotifyListener) this, notifyMessage);
    }

    protected void showInput(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void appToast(Object message) {
        AppMsg.Style style = new AppMsg.Style(1000, R.color.titleback);
        AppMsg appMsg = AppMsg.makeText(this, message.toString(), style);
        appMsg.setLayoutGravity(Gravity.CENTER_VERTICAL);
        appMsg.show();
    }

    protected boolean ensureBLEExists() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber,String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

}

