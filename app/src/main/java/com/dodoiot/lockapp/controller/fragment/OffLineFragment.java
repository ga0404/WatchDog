package com.dodoiot.lockapp.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
//import com.dodoiot.lockapp.controller.activity.AddCardActivity;
import com.dodoiot.lockapp.controller.activity.BaseFragment;
import com.dodoiot.lockapp.model.BaseBean;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.model.OffLinePwdBean;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.DateUtil;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.util.TShow;
//import com.dodoiot.lockapp.view.MyTipsDialog;
import com.dodoiot.lockapp.view.ProgressDialog;
import com.dodoiot.lockapp.view.TimeSelector;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class OffLineFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @InjectView(R.id.editpwd)
    EditText editpwd;
    @InjectView(R.id.tvstarttime)
    TextView tvstarttime;
    @InjectView(R.id.starttimelayout)
    LinearLayout starttimelayout;
    @InjectView(R.id.tvendtime)
    TextView tvendtime;
    @InjectView(R.id.endtimelayout)
    LinearLayout endtimelayout;
    @InjectView(R.id.btnsetpwd)
    Button btnsetpwd;
    ProgressDialog mTipDlg;

    private String mParam1;
    private String mParam2;
    boolean isName = false;
    boolean isStart = false;
    boolean isEnd = false;

    DeviceBean bean;
    public static OffLineFragment newInstance(String param1, DeviceBean bean) {
        OffLineFragment fragment = new OffLineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            bean = (DeviceBean) getArguments().getSerializable(ARG_PARAM2);
        }
    }




    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_off_line;
    }

    @Override
    protected void initView(View view) {
        mTipDlg = new ProgressDialog(getActivity(),getResources().getString(R.string.tips_load_message));
        mTipDlg.setCancelable(false);



    }

    @Override
    protected void initListener() {
//        editpwd.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length()>0){
//                    isName = true;
//                }else{
//                    isName = false;
//                }
//                show();
//            }
//        });
//        tvstarttime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length()>0){
//                    isStart = true;
//                }else{
//                    isStart = false;
//                }
//                show();
//            }
//        });
//        tvendtime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length()>0){
//                    isEnd = true;
//                }else{
//                    isEnd = false;
//                }
//                show();
//            }
//        });

    }

    private void show(){
        if(isStart&& isEnd && isName){
            btnsetpwd.setBackgroundResource(R.drawable.btnback_selector);
        }else{
            btnsetpwd.setBackgroundResource(R.drawable.btn_register_background1);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        long startTime  = System.currentTimeMillis();
        tvstarttime.setText(DateUtil.getDateStringByTimeMillis1(startTime));
        tvendtime.setText(DateUtil.getDateStringByTimeMillis1(startTime+24*60*60*1000));
    }

    int showType = 0;
    @OnClick({R.id.starttimelayout,R.id.endtimelayout,R.id.btnsetpwd})
    protected void onClickView(View v){
        switch (v.getId()){
            case R.id.starttimelayout:
                showType = 0;
                showTime();
                break;
            case R.id.endtimelayout:
                showType= 1;
                showTime();
                break;
            case R.id.btnsetpwd:
                //if(isStart&& isEnd && isName)
                getOffPwd();
                break;
        }
    }

    private TimeSelector timeSelector;
    private void showTime() {
        if (timeSelector == null)
            timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    if (showType == 0) {
                        tvstarttime.setText(time);
                    } else {
                        tvendtime.setText(time);


                    }
                }
            }, tvstarttime.getText().toString(), "2030-12-31 23:59","yyyy-MM-dd HH:mm");
        timeSelector.setMode(TimeSelector.MODE.YMDHM);
        if(showType == 0){
            timeSelector.setTitle(getResources().getString(R.string.starttime));
        }else{
            timeSelector.setTitle(getResources().getString(R.string.endtime));
        }
        timeSelector.show();
    }


    private void getOffPwd(){

        mTipDlg.show();
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        params.put("sn",bean.getSn());
//        params.put("fromtime",""+DateUtil.getTimeMillis(tvstarttime.getText().toString().trim(),"yyyy-MM-dd HH:mm:ss"));
//        params.put("totime",""+DateUtil.getTimeMillis(tvendtime.getText().toString().trim(),"yyyy-MM-dd HH:mm:ss"));
        params.put("fromtime",tvstarttime.getText().toString().trim()+":00");
        params.put("totime",tvendtime.getText().toString().trim()+":00");
        RequestManager.requestString(getActivity(), CommonUrl.GetOfflineLockPwd,params,parser,getActivity(),true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            setDefault();
            if(null != string && !string.equals("")){
                Log.e("dfc","<string------->"+string);
                OffLinePwdBean bean = JsonUtils.getGson().fromJson(string,OffLinePwdBean.class);
                if(bean.getCode() == 0){
                    editpwd.setText(bean.getPassword());
                    StringBuffer sb = new StringBuffer();
                    sb.append(getResources().getString(R.string.tips_allpwd));
                    sb.append(bean.getPassword()+"\r\n");
                    sb.append(getResources().getString(R.string.validtime)+tvstarttime.getText().toString()+"\r\n");
                    sb.append(getResources().getString(R.string.overtime)+tvstarttime.getText().toString()+"\r\n");
                    sb.append(getResources().getString(R.string.tips_allpwd2));
                    final  String msg = sb.toString();
//                    MyTipsDialog.showMyDailog(getActivity(),sb.toString(),"", new MyTipsDialog.IDialogMethod() {
//                        @Override
//                        public void sure() {
//                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
//                            intent.putExtra("sms_body", msg);
//                            startActivity(intent);
//                        }
//
//                    });

                }else{
                    TShow.showToast(getActivity(),R.string.tips_getoffline_failed);
                }
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


}
