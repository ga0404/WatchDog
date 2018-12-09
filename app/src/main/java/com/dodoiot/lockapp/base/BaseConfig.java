package com.dodoiot.lockapp.base;

import android.os.Environment;

public class BaseConfig {

    public static final int SELECTCITY = 101;

    public static boolean isDebug = true;

    public static String SAVEUSER = "saveuser";
    public static String USERINFO = "userinfo";
    public static String SAVECOMPANY = "savecompany";
    public static String COMPANYINFO = "companyinfo";
    public static String LOGININFO = "logininfo";
    public static String ISSAVELOGIN = "issavelogin";
    public static String ACCOUNT = "account";
    public static String PASSWORD = "password";

    public static String SAVETOKEN ="tokeninfo";
    public static String TOKEN ="token";


    public static String POSDEVICENUM = "posdevicenum";
    public static String POSINPUTTYPE = "type";

    public static String VERBATEMSG = "verbatemsg";
    public static String VERBATE = "verbate";

    public static String MEDIAMSG = "mediamsg";
    public static String MEDIA ="media";

    public static String POWERMSG = "powermsg";

    public static String LOCALPATH = Environment.getExternalStorageDirectory()
            + "/duoduohouse";



    public static String MESSAGE="message";

    public static String SEARCHTYPE = "searchtype";

    public static int SEARCHZK = 101;

    public static int SEARCHZNS = 103;


    public static int CHOOSEROOM = 102;

    public static int SEARCHEMPLOYEE = 104;


    public static int SEARCHSUO = 105;
}
