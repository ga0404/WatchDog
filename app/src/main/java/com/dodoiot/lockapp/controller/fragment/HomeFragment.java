package com.dodoiot.lockapp.controller.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
import com.dodoiot.lockapp.controller.activity.BaseFragment;
import com.dodoiot.lockapp.controller.activity.InputAddActivity;
import com.dodoiot.lockapp.controller.activity.LockListActivity;
import com.dodoiot.lockapp.controller.adapter.MyPageAdapter;
import com.dodoiot.lockapp.model.DeviceListBean;
import com.dodoiot.lockapp.model.GroupDeviceListBean;
import com.dodoiot.lockapp.model.GroupListBean;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.util.TShow;
import com.dodoiot.lockapp.view.ProgressDialog;
import com.dodoiot.lockapp.view.SlidingTabLayout;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class HomeFragment extends BaseFragment {


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


    MyPageAdapter adapter;
    GroupDeviceListBean groupDeviceListBean;
    List<DeviceListBean> list = new ArrayList<>();
    List<LockFragment> listFragment = new ArrayList<>();
    ProgressDialog mTipDlg;
    String branchMsg;
    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {

        mTipDlg = new ProgressDialog(getActivity(),getResources().getString(R.string.tips_load_message));
        mTipDlg.setCancelable(false);

        btnleft.setVisibility(View.INVISIBLE);
        tvtitle.setText(R.string.mydevice);
        btnright.setVisibility(View.GONE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(R.string.add);

//
//        adapter = new MyPageAdapter(getChildFragmentManager(),list,listFragment);
//        viewpager.setAdapter(adapter);
//        slidinglayout.setViewPager(viewpager);
        //loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void initListener() {
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPop();
            }
        });
    }

    public void loadData(){
//        type = 0;
        mTipDlg.show();
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        RequestManager.requestString(getActivity(), CommonUrl.DeviceList,params,parser,getActivity(),true);
    }


    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            setDefault();
            if(null != string && !string.equals("")){
                {
                    Log.e("dfc","string---homeFragment--->"+string);
                    GroupDeviceListBean bean = JsonUtils.getGson().fromJson(string,GroupDeviceListBean.class);
                    if(bean.getCode() == 0){
                        branchMsg = string;
                        if(list.size()>0){
                            list.clear();
                            list.addAll(bean.getGrouplist());
                            listFragment.clear();
                            for(int i=0;i<bean.getGrouplist().size();i++){
                                listFragment.add(LockFragment.newStances(i,string,list.get(i)));
                            }
//                            adapter.addAll(bean.getGrouplist());
                            adapter.notifyDataSetChanged();
                            String[] ss = new String[list.size()];
                            for(int i=0;i<list.size();i++){
                                ss[i] = list.get(i).getName();
                            }
                            slidinglayout.setViewPager(viewpager,ss);
                        }else{
                            list.clear();
                            list.addAll(bean.getGrouplist());
                            for(DeviceListBean b:bean.getGrouplist()){
                                listFragment.add(new LockFragment());
                            }
                            adapter = new MyPageAdapter(getChildFragmentManager(),list,listFragment,string);
//                      viewpager.setOffscreenPageLimit(3);
                            viewpager.setAdapter(adapter);
                            String[] ss = new String[list.size()];
                            for(int i=0;i<list.size();i++){
                                ss[i] = list.get(i).getName();
                            }
                            slidinglayout.setViewPager(viewpager,ss);
                            //slidinglayout.setViewPager(viewpager);
                        }

                        groupDeviceListBean = bean;
                        Log.e("dfc","currentiten------->"+viewpager.getCurrentItem());
                    }
                }
            }else{

            }
        }

        @Override
        public void parseError(VolleyError error) {
            TShow.showToast(getActivity(),R.string.connect_failed_tips);
            setDefault();
        }

        @Override
        public void parseDialog(int type) {
            if(type == 1){
                setDefault();
            }

        }
    };

    private void setDefault(){
        if(mTipDlg != null)
            mTipDlg.dismiss();
    }

    private PopupWindow pop;
    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.layout_bottom_dialog, null);
        TextView mAlbum = (TextView) bottomView.findViewById(R.id.tv_album);
        TextView mCamera = (TextView) bottomView.findViewById(R.id.tv_camera);
        TextView mADDBle = (TextView) bottomView.findViewById(R.id.tvaddble);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        toInput();
                        break;
                    case R.id.tv_camera:
                        break;
                    case R.id.tvaddble:
                        toADD();
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
        mADDBle.setOnClickListener(clickListener);
    }

    public void toInput(){
        Intent intent = new Intent(getActivity(), InputAddActivity.class);
        intent.putExtra("groupid",groupDeviceListBean.getGrouplist().get(viewpager.getCurrentItem()).getId());
        startActivityForResult(intent,101);
    }

    public void toADD(){
        Intent intent = new Intent(getActivity(), LockListActivity.class);
        intent.putExtra("groupid",groupDeviceListBean.getGrouplist().get(viewpager.getCurrentItem()).getId());
        startActivityForResult(intent,101);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != getActivity().RESULT_OK)
            return;
        Log.e("dfc","<-----requestCode-------->"+requestCode);
        //loadData();
    }
}
