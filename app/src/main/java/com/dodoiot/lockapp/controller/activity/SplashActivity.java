package com.dodoiot.lockapp.controller.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        String token = Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN);;

        if(null != token && !token.equals("")){
            toLogin();
        }else{
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }

    }


    private void toLogin(){
        String pwd = Gloal.m_spu_login.loadStringSharedPreference(BaseConfig.PASSWORD);
        String name = Gloal.m_spu_login.loadStringSharedPreference(BaseConfig.ACCOUNT);

        String pass = StringUtils.stringMD5(name + pwd);
        Map<String,String> params = new HashMap<>();
        params.put("username",name);
        params.put("pwd",pass);
        RequestManager.requestString(SplashActivity.this, CommonUrl.Login,params,parser,SplashActivity.this,true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {

            if(null != string && !string.equals("")){
                try{
                    Log.e("dfc","string--------------->"+string);
                    LoginBean bean = JsonUtils.getGson().fromJson(string,LoginBean.class);
                    if(bean.getCode() == 0){

                        Gloal.m_spu_token.saveSharedPreferences(BaseConfig.TOKEN,bean.getToken());
                        String userinfo = JsonUtils.getGson().toJson(bean.getUserinfo());
                        Gloal.m_spu_userinfo.saveSharedPreferences(BaseConfig.USERINFO,userinfo);
//                        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.PASSWORD,editpassword.getText().toString().trim());
//                        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ACCOUNT,editaccount.getText().toString().trim());
//                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                        startActivity(intent);
                        finish();
                    }else if(bean.getCode() == 100){
                        //toLogin();
                        setDefault();
                        //TShow.showToast(LoginActivity.this,R.string.tips_login_error);
                    }else{
                        //toLogin();
                        setDefault();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void parseError(VolleyError error) {
            //appToast(getResources().getString(R.string.connect_failed_tips));
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
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        finish();
    }


}
