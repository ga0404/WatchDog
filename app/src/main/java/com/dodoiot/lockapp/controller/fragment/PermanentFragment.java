package com.dodoiot.lockapp.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dodoiot.lockapp.R;
//import com.dodoiot.lockapp.controller.activity.AuthActivity;
import com.dodoiot.lockapp.controller.activity.BaseFragment;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.model.LockMsgBean;
import com.dodoiot.lockapp.util.JsonUtils;
//import com.dodoiot.lockapp.view.MyTipsDialog;
import com.dodoiot.lockapp.view.ProgressDialog;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PermanentFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @InjectView(R.id.editpwd)
    EditText editpwd;
    @InjectView(R.id.getCode)
    Button getCode;

    ProgressDialog mTipDlg;

    private String mParam1;
    DeviceBean bean;

    public static PermanentFragment newInstance(String param1, DeviceBean bean) {
        PermanentFragment fragment = new PermanentFragment();
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
        layoutId = R.layout.fragment_permanent;
    }

    @Override
    protected void initView(View view) {
        mTipDlg = new ProgressDialog(getActivity(),getResources().getString(R.string.tips_load_message));
        mTipDlg.setCancelable(false);

    }

    @Override
    protected void initListener() {
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTipDlg.show(2000);
                getCode();
            }
        });
    }

    private void getCode(){
        editpwd.setText(getPwd());
//        MyTipsDialog.showMyDailog(getActivity(),getResources().getString(R.string.tips_allpwd)+getPwd()+"\r\n"+getResources().getString(R.string.tips_allpwd2),"", new MyTipsDialog.IDialogMethod() {
//            @Override
//            public void sure() {
//                String msg = getResources().getString(R.string.tips_allpwd)+getPwd()+getResources().getString(R.string.tips_allpwd2);
//                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
//                intent.putExtra("sms_body", msg);
//                startActivity(intent);
//            }
//
//        });
    }


    private  String getPwd(){
        String str="";
        String pass = bean.getFixedPassword();
        Log.e("dfc","pass-------->"+pass);

        Type listType = new TypeToken<List<LockMsgBean.PwdListBean>>(){}.getType();
        //Gson gson = new Gson();
        List<LockMsgBean.PwdListBean> list = JsonUtils.getGson().fromJson(pass, listType);

        //List<LockMsgBean.PwdListBean> list = (List<LockMsgBean.PwdListBean>) JsonUtils.getGson().fromJson(pass, LockMsgBean.PwdListBean.class);
        for(LockMsgBean.PwdListBean bean:list){
            if(bean.getType() == 2){
                str = bean.getValue();
                break;
            }
        }
        Log.e("dfc","str-------->"+str);
        return str;
    }

}
