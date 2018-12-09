package com.dodoiot.lockapp.controller.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends FragmentActivity {
    protected int layoutId;
    protected TextView tvTitle;
    protected Button btnLeft, btnRight;
    protected boolean isFull = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayoutId();
        if (layoutId == 0)
            return;
        if (!isFull)
            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        else
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutId);
        ButterKnife.inject(this);
//        if (!isFull){
//            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                    R.layout.layout_title);
//            tvTitle = (TextView) findViewById(R.id.tvtitle);
//            btnLeft = (Button) findViewById(R.id.btnleft);
//            btnRight = (Button) findViewById(R.id.btnright);
//        }
        initView();
        initListener();
    }


    protected abstract void initLayoutId();

    protected abstract void initView();

    protected abstract void initListener();


    protected void setTitleId(int titleId) {
        if (tvTitle != null)
            tvTitle.setText(titleId);
    }


}

