package com.dodoiot.lockapp.controller.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.controller.fragment.HomeFragment;
import com.dodoiot.lockapp.controller.fragment.SetFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseFragmentActivity implements RadioButton.OnCheckedChangeListener {

    private static final int TAB_MYDEVICE = 0;
    private static final int TAB_MESSAGE = 1;

    private HomeFragment homeFragment;
    private SetFragment setFragment;

    @InjectView(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @InjectView(R.id.RadioButton0)
    RadioButton RadioButton0;
    @InjectView(R.id.RadioButton1)
    RadioButton RadioButton1;

    private Fragment mContent;
    private FragmentManager fragmentManager;
    private int currentindex = 0;
    public static MainActivity mainActivity;

    @Override
    protected void initLayoutId() {
        isFull = true;
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mainActivity = this;

        fragmentManager = getSupportFragmentManager();
        RadioButton0 = (RadioButton) findViewById(R.id.RadioButton0);
        RadioButton1 = (RadioButton) findViewById(R.id.RadioButton1);
        playIndex(currentindex);
    }

    @Override
    protected void initListener() {
        RadioButton0.setOnCheckedChangeListener(this);
        RadioButton0.setChecked(true);
        RadioButton1.setOnCheckedChangeListener(this);
    }
    public void playIndex(int index) {
        Fragment newContent = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        hideFragments(transaction);
        switch (index) {
            case TAB_MYDEVICE:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.realtabcontent, homeFragment);
                }else{
                }
                newContent = homeFragment;
                break;
            case TAB_MESSAGE:
                if (setFragment == null) {
                    setFragment = new SetFragment();
                    transaction.add(R.id.realtabcontent, setFragment);
                }else{

                }
                newContent = setFragment;
                break;

        }
        if (newContent != null) {
            transaction.show(newContent);
//          transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != homeFragment) {
            transaction.hide(homeFragment);
        }
        if (null != setFragment) {
            transaction.hide(setFragment);
        }

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }
        RadioButton0.setChecked(false);
        RadioButton1.setChecked(false);
        buttonView.setChecked(true);
        switch (buttonView.getId()){
            case R.id.RadioButton0:
                currentindex = 0;
//                setTitleId(R.string.mydevice);
                break;
            case R.id.RadioButton1:
                currentindex = 1;
//                 setTitleId(R.string.adddevice);
                break;
        }
        playIndex(currentindex);
    }
}
