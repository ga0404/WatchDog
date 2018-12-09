package com.dodoiot.lockapp.base;

import android.content.Context;
import android.content.res.Resources;

import com.dodoiot.lockapp.util.SharePerfenceUtils;

import java.io.File;

public class Gloal {
    public static Context m_ctx;
    public static Resources m_res;
    public static SharePerfenceUtils m_spu_token;
    public static SharePerfenceUtils m_spu_userinfo;
    public static SharePerfenceUtils m_spu_login;
    public static SharePerfenceUtils m_spu_company;
    public static SharePerfenceUtils m_spu_verbate;
    public static SharePerfenceUtils m_spu_media;
    public static SharePerfenceUtils m_spu_power;
    public static SharePerfenceUtils m_spu_exit;

    public static void onAppStart(Context ctx){
        m_ctx = ctx;
        m_res = ctx.getResources();

        m_spu_token = new SharePerfenceUtils(m_ctx,BaseConfig.SAVETOKEN);
        m_spu_login = new SharePerfenceUtils(m_ctx, BaseConfig.LOGININFO);
//        m_spu_exit = new SharePerfenceUtils(m_ctx,BaseConfig.SAVEEXIT);
        m_spu_userinfo = new SharePerfenceUtils(m_ctx,BaseConfig.SAVEUSER);
        m_spu_company = new SharePerfenceUtils(m_ctx,BaseConfig.SAVECOMPANY);
        m_spu_verbate = new SharePerfenceUtils(m_ctx,BaseConfig.VERBATEMSG);
        m_spu_media = new SharePerfenceUtils(m_ctx,BaseConfig.MEDIAMSG);
        m_spu_power = new SharePerfenceUtils(m_ctx,BaseConfig.POWERMSG);

        initDir();
    }


    private static void initDir(){
        File file = new File(BaseConfig.LOCALPATH);
        if(!file.exists()){
            file.mkdir();
        }
    }
}
