package com.dodoiot.lockapp.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
import com.dodoiot.lockapp.controller.activity.BaseFragment;
//import com.dodoiot.lockapp.controller.activity.BranchActivity;
import com.dodoiot.lockapp.controller.activity.LoginActivity;
//import com.dodoiot.lockapp.controller.activity.PersionActivity;
import com.dodoiot.lockapp.model.BaseBean;
import com.dodoiot.lockapp.model.UserInfoBean;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.view.CircleImageView;
//import com.dodoiot.lockapp.view.MyTipsDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SetFragment extends BaseFragment {


    @InjectView(R.id.headimage)
    CircleImageView headimage;
    @InjectView(R.id.tvphone)
    TextView tvphone;
    @InjectView(R.id.tvaccount)
    TextView tvaccount;
    @InjectView(R.id.tips)
    TextView tips;
    @InjectView(R.id.personlayout)
    RelativeLayout personlayout;
    @InjectView(R.id.fendianlayout)
    RelativeLayout fendianlayout;
    @InjectView(R.id.exitlayout)
    RelativeLayout exitlayout;
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

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_set;
    }

    @Override
    protected void initView(View view) {
        leftlayout.setVisibility(View.GONE);
        rightlayout.setVisibility(View.GONE);
        tvtitle.setText(R.string.set);
    }

    @Override
    public void onResume() {
        super.onResume();
        String info = Gloal.m_spu_userinfo.loadStringSharedPreference(BaseConfig.USERINFO);
        UserInfoBean bean = JsonUtils.getGson().fromJson(info,UserInfoBean.class);
        if(null != bean.getNickname() && !"".equals(bean.getNickname())){
            tvphone.setText(bean.getNickname());
        }else{
            tvphone.setText(bean.getMobile());
        }
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.personlayout, R.id.fendianlayout, R.id.exitlayout})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.personlayout:
                toPersion();
                break;
            case R.id.exitlayout:
                showExitDialog();
                break;
            case R.id.fendianlayout:
                toBranch();
                break;
        }
    }

    private void toPersion() {
//        Intent intent = new Intent(getActivity(), PersionActivity.class);
//        startActivity(intent);

    }

    private void toBranch() {
//        Intent intent = new Intent(getActivity(), BranchActivity.class);
//        startActivityForResult(intent,103);
    }


    private void showExitDialog() {
//        MyTipsDialog.showExitDialog(getActivity(),R.string.tips_exit, new MyTipsDialog.IDialogInputNoteMethod() {
//            @Override
//            public void sure(String note) {
//                exit();
//            }
//        });
    }

    private void exit(){
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        RequestManager.requestString(getActivity(), CommonUrl.EXIT,params,parser,getActivity(),true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            // setDefault();
            //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            if(null != string && !string.equals("")){
                BaseBean bean = JsonUtils.getGson().fromJson(string,BaseBean.class);
                if(bean.getCode() == 0){
                    toLogin();
                }else{
                    toLogin();
                }
            }
        }

        @Override
        public void parseError(VolleyError error) {
            //TShow.showToast(SystemSetActivity.this,R.string.connect_failed_tips);
            //setDefault();
            toLogin();
        }

        @Override
        public void parseDialog(int type) {
            if(type == 1){
                //setDefault();
            }

        }
    };


    private void toLogin(){
        Gloal.m_spu_token.saveSharedPreferences(BaseConfig.TOKEN,"");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
