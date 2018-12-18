package com.dodoiot.lockapp.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.controller.activity.BaseFragment;
import com.dodoiot.lockapp.controller.activity.LockDetailsActivity;
import com.dodoiot.lockapp.controller.activity.ManagePwdActivity;
import com.dodoiot.lockapp.controller.adapter.LockAdapter;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.model.DeviceListBean;
import com.dodoiot.lockapp.util.DateUtil;
import com.dodoiot.lockapp.util.TShow;
import com.dodoiot.lockapp.view.swipe.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LockFragment extends BaseFragment {


    int opeationType;
    LockAdapter adapter;
    DeviceListBean deviceListBean;
    List<DeviceBean> listData = new ArrayList<>();
    @InjectView(R.id.locklistview)
    SwipeMenuListView locklistview;
    String branchMsg;
    public static LockFragment newStances(int type, String branchMsg,DeviceListBean bean) {
        LockFragment fragment = new LockFragment();
        Bundle b = new Bundle();

        b.putInt("type", type);
        b.putString("msg",branchMsg);
        b.putSerializable("bean", bean);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_lock;
    }

    @Override
    protected void initView(View view) {
        Bundle args = getArguments();
        if (args != null) {
            opeationType = args.getInt("type");
            branchMsg = args.getString("msg");
            deviceListBean = (DeviceListBean) args.getSerializable("bean");
        }
        listData.clear();
        listData.addAll(deviceListBean.getDevList());
        adapter = new LockAdapter(getActivity(), listData);
        locklistview.setAdapter(adapter);
    }


    public void updateArguments(int pageType, String branchMsg,DeviceListBean bean) {
        this.opeationType = pageType;
        this.branchMsg = branchMsg;
        deviceListBean = bean;

        Bundle b = getArguments();
        if (b != null) {
            b.putInt("type", pageType);
            b.putString("msg",branchMsg);
            b.putSerializable("bean", bean);
        }

        if(adapter != null){
            listData.clear();
            listData.addAll(deviceListBean.getDevList());
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void initListener() {
        locklistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeviceBean bean = (DeviceBean) adapter.getItem(i);
                toDetails(bean);
            }
        });
    }

    private void toDetails(DeviceBean bean){
        boolean isAuth = false;
        if(bean.getValideFrom()==null || bean.getValideFrom().equals("")){
            isAuth = false;
        }else{
            long currentTime = DateUtil.getCurrentTimeMills();
            long toTime = Long.parseLong(bean.getValideTo());
            if(currentTime > toTime){
                TShow.showToast(getActivity(),R.string.tips_auth_timemout);
                return;
            }
            isAuth = true;
        }

        Intent intent = new Intent(getActivity(), LockDetailsActivity.class);
        intent.putExtra("msg",branchMsg);
        intent.putExtra("auth",isAuth);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        intent.putExtras(bundle);
        startActivityForResult(intent,103);

    }
}
