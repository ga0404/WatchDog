package com.dodoiot.lockapp.util;

import android.widget.EditText;

import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.Gloal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public synchronized static boolean isNullStr(String str) {
        boolean isOK = false;
        if (str == null || "".equals(str.trim())) {
            isOK = true;
        }
        return isOK;
    }

    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }



    public static String getTextString(EditText edit){
        if(edit == null)
            return "";
        return edit.getText().toString().trim();
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断手机号
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[7])|(18[0,1,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    public static String T(Object t) {
        String _msg = "";
        if (t instanceof Integer) {
            _msg = Gloal.m_res.getString((Integer) t);
        } else if (t instanceof String) {
            _msg = (String) t;
        }

        return _msg;
    }

    /**
     * MD5加密
     * @param input
     * @return
     */
    public static String stringMD5(String input)
    {
        try
        {
            byte[] btInput = input.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = btInput;
            messageDigest.update(inputByteArray);
            messageDigest.update(messageDigest.digest());
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);

        } catch (NoSuchAlgorithmException e)
        {
            return null;
        }

    }

    public static String byteArrayToHex(byte[] byteArray)
    {
        char[] hexDigits =
                { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray)
        {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }


//    /**
//     * 自定义规则生成32位编码
//     * @return string
//     */
//    public static String getUUIDByRules(String rules)
//    {
//        int rpoint = 0;
//        StringBuffer generateRandStr = new StringBuffer();
//        Random rand = new Random();
//        int length = 32;
//        for(int i=0;i<length;i++)
//        {
//            if(rules!=null){
//                rpoint = rules.length();
//                int randNum = rand.nextInt(rpoint);
//                generateRandStr.append(radStr.substring(randNum,randNum+1));
//            }
//        }
//        return generateRandStr+"";
//    }


    public static void saveData(boolean isSaveData,String name,String pwd) {
        boolean isSaveData1 = Gloal.m_spu_login.loadBooleanSharedPreference(BaseConfig.ISSAVELOGIN);
        if (isSaveData) {
            Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ISSAVELOGIN, true);
            Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ACCOUNT, name);
            Gloal.m_spu_login.saveSharedPreferences(BaseConfig.PASSWORD, pwd);
        } else {//默认数据
            Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ISSAVELOGIN, false);
            Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ACCOUNT, "");
            Gloal.m_spu_login.saveSharedPreferences(BaseConfig.PASSWORD, "");
        }
    }

    public static void saveClearData(){
        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ISSAVELOGIN, false);
        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.ACCOUNT, "");
        Gloal.m_spu_login.saveSharedPreferences(BaseConfig.PASSWORD, "");
        Gloal.m_spu_userinfo.saveSharedPreferences(BaseConfig.USERINFO,"");
    }

}
