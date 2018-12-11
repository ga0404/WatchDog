package com.dodoiot.lockapp.base;

public class CommonUrl {

//    public static String BASE_API = "https://www.dodoiot.com/app-test/smartlock/app/v1/";
// var BASE_API = "http://localhost:8888/smartlock/app/v1/";
    public static String BASE_API = "http://47.95.201.150:8889/smartlock/app/v1/";


    public static String WxAuthorize = BASE_API + "user/wxAuthorize";

    public static String WxLogin = BASE_API + "user/dowxlogin";

    public static String WxBind =  BASE_API + "user/wxbind";
    public static String Login = BASE_API + "user/dologin?";
    public static String EXIT = BASE_API +"user/dologout?";
    public static String DeviceList = BASE_API + "device/list?";
    public static String OpenDoor = BASE_API + "device/locker/opendoor?";
    public static String AddPassword =  BASE_API + "device/locker/genpassword?";
    public static String getOfflinePassword = BASE_API + "device/locker/offlinepassword";
    public static String GetSMS = BASE_API + "user/smscode";
    public static String Regist = BASE_API + "user/doregist?";
    public static String RESETPWD = BASE_API +"user/resetpassword?";
    public static String AddLocker = BASE_API + "device/locker/add?";
    public static String RemoveLocker = BASE_API + "device/locker/delete?";
    public static String GetLockerOpenDoorRecord =  BASE_API + "device/locker/opendoorlist";

    public static String ListLockerCard = BASE_API + "device/locker/card/list?";
    public static String AddLockerCard = BASE_API + "device/locker/card/add?";
    public static String EditLockerCard = BASE_API + "device/locker/card/edit?";
    public static String DeleteLockerCard = BASE_API + "device/locker/card/delete?";

    public static String ListLockerPassword = BASE_API + "device/locker/password/list?";
    public static String AddLockerPassword =  BASE_API + "device/locker/password/add?";

    public static String EditLockerPassword =  BASE_API + "device/locker/password/edit";
    public static String DeleteLockerPassword =  BASE_API + "device/locker/password/delete?";
    public static String OfflineLockerPassword = BASE_API + "device/locker/password/getoffline";

    public static String AddBleLocker =  BASE_API + "device/bleLocker/add?";
    public static String SaveBleLocker =  BASE_API + "device/bleLocker/save?";
    public static String AddBleLockerPassword = BASE_API + "device/locker/password/addble?";
    public static String AddBleLockerCard = BASE_API + "device/locker/card/addble?";
    public static String EditBleLockerCard = BASE_API + "device/locker/card/editble?";

    public static String EditLocker = BASE_API + "device/locker/edit?";
    public static String ModifyPassword = BASE_API + "user/modifypassword?";
    public static String ModifyNickname = BASE_API + "user/editNickname?";

    public static String GroupList = BASE_API + "device/group/list?";
    public static String AddGroup = BASE_API + "device/group/add?";
    public static String UpdateGroup = BASE_API + "device/group/update?";
    public static String DelGroup = BASE_API + "device/group/del?";

    public static String GetOpLogList = BASE_API + "device/oplog/list?";
    public static String AddOpLog = BASE_API + "device/oplog/add?";
    public static String DeleteOplog = BASE_API + "device/oplog/del?";

    public static String UserAuth = BASE_API + "device/locker/userAuth?";
    public static String GetAuthList = BASE_API + "device/auth/list?";
    public static String UpdateAuth = BASE_API + "device/auth/edit?";
    public static String DelAuth = BASE_API + "device/auth/del?";

    public static String GetOfflineLockPwd = BASE_API + "device/blelock/getofflinepwd?";
    public static String DelAllLockPassword = BASE_API + "device/locker/password/delAll?";


}
