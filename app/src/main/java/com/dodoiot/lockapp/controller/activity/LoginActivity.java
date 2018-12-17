package com.dodoiot.lockapp.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
import com.dodoiot.lockapp.model.LoginBean;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.util.StringUtils;
import com.dodoiot.lockapp.view.ProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.editaccount)
    EditText editaccount;
    @InjectView(R.id.clear)
    ImageView clear;
    @InjectView(R.id.editpassword)
    EditText editpassword;
    @InjectView(R.id.showpass)
    ImageView showpass;
    @InjectView(R.id.tvforget)
    TextView tvforget;
    @InjectView(R.id.btnlogin)
    Button btnlogin;
    @InjectView(R.id.tvregister)
    TextView tvregister;
    @InjectView(R.id.layoutcontent)
    LinearLayout layoutcontent;
    @InjectView(R.id.activity_login)
    RelativeLayout activityLogin;

    boolean isShowPassWord = false;
    @Override
    protected void initLayoutId() {
        isFull = true;
        layoutId = R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mTipDlg = new ProgressDialog(LoginActivity.this,getResources().getString(R.string.tips_login_loading));
        mTipDlg.setCancelable(false);
        editaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    clear.setVisibility(View.VISIBLE);
                }else{
                    clear.setVisibility(View.GONE);
                }

            }
        });
        editpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    showpass.setVisibility(View.VISIBLE);
                }else{
                    showpass.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //String info = Gloal.m_spu_userinfo.loadStringSharedPreference(BaseConfig.USERINFO);
        String pwd = Gloal.m_spu_login.loadStringSharedPreference(BaseConfig.PASSWORD);
        String name = Gloal.m_spu_login.loadStringSharedPreference(BaseConfig.ACCOUNT);
        if(null != pwd && !pwd.equals("")){
            editaccount.setText(name);
            editpassword.setText(pwd);
        }
    }


    @OnClick({R.id.btnlogin, R.id.clear, R.id.showpass, R.id.tvforget, R.id.tvregister})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin:
                toLogin();
                break;
            case R.id.clear:
                editaccount.setText("");
                break;
            case R.id.showpass:
                showPass();
                break;
            case R.id.tvforget:
                toForget();
                break;
            case R.id.tvregister:
                toRegister();
                break;
        }
    }

    private boolean isEmpty(){
        boolean isEmpty = true;
        if(StringUtils.isEmpty(editaccount.getText().toString().trim())){
            isEmpty = false;
            appToast(getResources().getString(R.string.tips_login_account_null));
            //TShow.showToast(LoginActivity.this,R.string.tips_login_account_null);
            return isEmpty;
        }

        if(StringUtils.isEmpty(editpassword.getText().toString().trim())){
            isEmpty = false;
            appToast(getResources().getString(R.string.tips_login_password_null));
            //TShow.showToast(LoginActivity.this,R.string.tips_login_password_null);
            return isEmpty;
        }
        /**
         * 缺少 手机格式或邮箱格式 验证
         */
        return isEmpty;
    }

    private void toLogin(){
        //Log.e("dfc","<-------------tologin111----------->");
        if(!isEmpty())
            return;
        mTipDlg.show();
        String name = StringUtils.getTextString(editaccount);
        String password = StringUtils.getTextString(editpassword);
        String pass = StringUtils.stringMD5(name + password);
        Map<String,String> params = new HashMap<>();
        params.put("username",editaccount.getText().toString().trim());
        params.put("pwd",pass);
        RequestManager.requestString(LoginActivity.this, CommonUrl.Login,params,parser,LoginActivity.this,true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            setDefault();
            if(null != string && !string.equals("")){
                try{
                    Log.e("dfc","string--------------->"+string);
                    LoginBean bean = JsonUtils.getGson().fromJson(string,LoginBean.class);
                    if(bean.getCode() == 0){

                        Gloal.m_spu_token.saveSharedPreferences(BaseConfig.TOKEN,bean.getToken());
                        String userinfo = JsonUtils.getGson().toJson(bean.getUserinfo());
                        Gloal.m_spu_userinfo.saveSharedPreferences(BaseConfig.USERINFO,userinfo);
                        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.PASSWORD,editpassword.getText().toString().trim());
                        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ACCOUNT,editaccount.getText().toString().trim());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(bean.getCode() == 100){
                        appToast(getResources().getString(R.string.tips_login_error));
                        //TShow.showToast(LoginActivity.this,R.string.tips_login_error);
                    }else{

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void parseError(VolleyError error) {
            appToast(getResources().getString(R.string.connect_failed_tips));
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


    private void showPass() {
        if (isShowPassWord) {
            isShowPassWord = false;
            editpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showpass.setBackgroundResource(R.mipmap.fcloseeye);
        } else {
            isShowPassWord = true;
            editpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showpass.setBackgroundResource(R.mipmap.feye);
        }
    }

    private void toForget() {
//        Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
//        startActivityForResult(intent, 101);
    }

    public void toRegister() {
//        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//        startActivityForResult(intent, 102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        if(requestCode == 101){
            if(null != data){
                editaccount.setText(data.getStringExtra("account"));
                editpassword.setText(data.getStringExtra("pwd"));
            }
        }else if(requestCode == 102){
            if(null != data){
                editaccount.setText(data.getStringExtra("account"));
                editpassword.setText(data.getStringExtra("pwd"));
            }
        }
    }
}
