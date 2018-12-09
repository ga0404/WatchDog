package com.dodoiot.lockapp.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.manager.EventManager;

public abstract class BaseFragment extends Fragment {
    protected int layoutId;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initLayoutId();
        if(layoutId == 0)
            return new View(getContext());
        view = inflater.inflate(layoutId,null);
        ButterKnife.inject(this, view);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    protected abstract void initLayoutId();

    protected abstract void initView(View view);

    protected abstract void initListener();


    public View findViewById(int id){
        return view.findViewById(id);
    }



}

