package com.dodoiot.lockapp.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.controller.adapter.MyPageAdapter;
import com.dodoiot.lockapp.controller.adapter.SetPwdPagerAdapter;
import com.dodoiot.lockapp.controller.fragment.CustomFragment;
import com.dodoiot.lockapp.controller.fragment.OffLineFragment;
import com.dodoiot.lockapp.controller.fragment.PermanentFragment;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SendPwdActivity extends BaseFragmentActivity {

    @InjectView(R.id.btnleft)
    Button btnleft;
    @InjectView(R.id.btnopertaion)
    Button btnopertaion;
    @InjectView(R.id.leftlayout)
    LinearLayout leftlayout;
    @InjectView(R.id.btnright)
    Button btnright;
    @InjectView(R.id.btnRight)
    TextView btnRight;
    @InjectView(R.id.rightlayout)
    LinearLayout rightlayout;
    @InjectView(R.id.tvtitle)
    TextView tvtitle;
    @InjectView(R.id.titlelayout)
    RelativeLayout titlelayout;
    @InjectView(R.id.slidinglayout)
    SlidingTabLayout slidinglayout;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;

    SetPwdPagerAdapter adapter;
    DeviceBean bean;
    @Override
    protected void initLayoutId() {
        isFull = true;
        layoutId = R.layout.activity_send_pwd;
    }

    @Override
    protected void initView() {
        tvtitle.setText(R.string.sendpwd);
        bean = (DeviceBean) getIntent().getSerializableExtra("bean");

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.sendtimearray));
        List<Fragment> listFragment = new ArrayList<>();
        listFragment.add(PermanentFragment.newInstance("",bean));
        listFragment.add(OffLineFragment.newInstance("",bean));
        listFragment.add(CustomFragment.newInstance("",bean));

        adapter = new SetPwdPagerAdapter(getSupportFragmentManager(),list,listFragment);
        viewpager.setOffscreenPageLimit(list.size());
        viewpager.setAdapter(adapter);
        slidinglayout.setViewPager(viewpager);

    }


    @Override
    protected void initListener() {

    }
    @OnClick({R.id.btnleft,R.id.leftlayout})
    protected void onClickView(View v){
        switch (v.getId()){
            case R.id.leftlayout:
            case R.id.btnleft:
                finish();
                break;


        }
    }

}
