package com.dodoiot.lockapp.util;

import java.util.ArrayList;
import java.util.List;

public class BleUtils {
    //BLUETOOTH_COMMUNICATE_COMMAND;
    public static byte FIRMWARE_UPDATE_CMD_ID = (byte)0x01;      //升级
    public static byte SET_CONFIG_COMMAND_ID = (byte)0x02;       //设置参数
    public static byte NOTIFY_COMMAND_ID = (byte)0x03;          //主动通知
    public static byte SET_PASSWORD_ENCKEY  = (byte)0x05;   //设置加密字符串 数据:分块传输
    public static byte AUTHOR_COMMAND_ID = (byte)0x10;          //连接认证
    public static byte FACTORY_TEST_COMMAND_ID = (byte)0x20;    //测试
    public static byte TEST_COMMAND_ID =(byte) 0xFF;


    //FIRMWARE_UPDATE_KEY;
    public static byte KEY_ENTER_FIRMWARE_UPDATE_MODE  = (byte)0x01;
    public static byte KEY_ENTER_DFU_MODE_RET          = (byte)0x02;
    public static byte KEY_GET_FIRMWARE_VERSION_INFO   = (byte)0x11;
    public static byte KEY_RET_FIRMWARE_VERSION_INFO   = (byte)0x12;


    public static byte KEY_SETDEVICETIME = (byte)0x01;
    public static byte KEY_GETDEVICETIME = (byte)0x02;
    public static byte KEY_ONESETPWD = (byte)0x04;
    public static byte KEY_CLEARALLPWD = (byte)0x05;
    public static byte KEY_READCARD = (byte)0x06;
    public static byte KEY_ADDCARD = (byte)0x07;
    public static byte KEY_DELCARD = (byte)0x08;



    public static byte KEY_SETMANAGEPWD = (byte)0x12;
    public static byte KEY_CLEARPWD = (byte)0x13;
    public static byte KEY_SETALLPWD = (byte)0x14;
    public static byte KEY_SETINTERIMPWD = (byte)0x15;
    public static byte KEY_SETPWD = (byte)0x17;

    public static byte KEY_OPENDOOR = (byte)0x15;

    public static byte KEY_SETCONNECTPWD = (byte)0x0a;
    public static byte KEY_BIND_DEVICE = (byte)0x16;
    public static byte KEY_BIND_DEVICE_OVER = (byte)0x17;
    public static byte KEY_RELEASEBIND_DEVICE = (byte)0x18;
    public static byte KEYCLEARALL = (byte)0x19;

    public static byte KEY_GETPOWER = (byte)0x18;

    public static byte CRC = (byte)0x00;
    public static byte HEADER = (byte)0xdd;
    public static byte SEQ = (byte)0x01;



    public static byte[] getBindByte(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        //result[3]
        result[4] = SEQ;
        result[5] = AUTHOR_COMMAND_ID;
        result[6] = KEY_BIND_DEVICE;
        result[7] = (byte) 0x00;
        return result;
    }

    public static byte[] getBindByteOver(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = AUTHOR_COMMAND_ID;
        result[6] = KEY_BIND_DEVICE_OVER;
        result[7] = (byte) 0x00;
        return result;
    }

    public static byte[] releaseBindByte(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = AUTHOR_COMMAND_ID;
        result[6] = KEY_RELEASEBIND_DEVICE;
        result[7] = (byte) 0x00;
        return result;
    }

    public static byte[] getConnectPwdByte(String password){
        byte[] result = new byte[16];
        result[0] = HEADER;
        result[2] = (byte)0x0a;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_SETCONNECTPWD;
        result[7] = (byte) 0x08;
//        for(int i=0;i<password.length();i++){
//            result[8+i] = (byte)Integer.parseInt(password.substring(i,i+1));
//        }
        password = ConvertToASCII(password);
        for(int i=0;i<password.length()/2;i++){
            result[8+i] = (byte)((char)Integer.parseInt(password.substring(2*i,2*(i+1))));
        }
        return result;
    }

    public static byte[] setConnectPwdByte(String password){
        byte[] result = new byte[16];
        result[0] = HEADER;
        result[2] = (byte)0x0a;
        result[4] = SEQ;
        result[5] = AUTHOR_COMMAND_ID;
        result[6] = (byte)0x01;
        result[7] = (byte) 0x08;
        password = ConvertToASCII(password);
        for(int i=0;i<password.length()/2;i++){
            result[8+i] = (byte)((char)Integer.parseInt(password.substring(2*i,2*(i+1))));
        }
        return result;
    }

    public static String ConvertToASCII(String string)
    {
        StringBuilder sb = new StringBuilder();
        char[] ch = string.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            sb.append(Integer.valueOf(ch[i]).intValue()).append("");// 加空格
            // sb.append(Integer.valueOf(ch[i]).intValue());// 不加空格
            //Log.e("dfc", "<string---------->" + sb.toString());
        }
        return sb.toString();
    }

    public static byte[] getDeviceTimeBy(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_GETDEVICETIME;
        result[7] = (byte) 0x00;
        return result;
    }


    public static byte[] setDeviceTimeByte(String time){

        byte[] result = new byte[14];
        result[0] = HEADER;
        result[2] = (byte)0x08;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_SETDEVICETIME;
        result[7] = (byte) 0x06;

        String total = DateUtil.getDataFormat(time);
        String[] sss = total.split(" ");
        List<String> listTime = new ArrayList<>();
        for(int i = 0;i<sss.length;i++){
            if(i == 0){
                String[] ss = sss[0].split("-");
                for(String s:ss){
                    listTime.add(s);
                }
            }else{
                String[] ss = sss[1].split(":");
                for(String s:ss){
                    listTime.add(s);
                }
            }
        }
        int year = Integer.parseInt(listTime.get(0).substring(2,4));
        int month = Integer.parseInt(listTime.get(1));
        int day = Integer.parseInt(listTime.get(2));
        int hour = Integer.parseInt(listTime.get(3));
        int minute = Integer.parseInt(listTime.get(4));
        int second = Integer.parseInt(listTime.get(5));
        result[8] = intToByte(year);
        result[9] = intToByte(month);
        result[10] = intToByte(day);
        result[11] = intToByte(hour);
        result[12] = intToByte(minute);
        result[13] = intToByte(second);
        return result;
    }


    public static byte[] getTimeByte(){

        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_GETDEVICETIME;
        result[7] = (byte) 0x00;
        return result;
    }


    public static byte[] setManagePwdByte(String pwd){
        byte[] result = new byte[19];
        result[0] = HEADER;
        result[2] = (byte)0x0d;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_SETMANAGEPWD;
        result[7] = (byte) 0x0b;
        result[8] = (byte) 0xff;
        if(pwd.length() < 8){
            pwd = changeLength(pwd);
        }
        String pwd1 = pwd.substring(0,2);
        String pwd2 = pwd.substring(2,4);
        String pwd3 = pwd.substring(4,6);
        String pwd4 = pwd.substring(6,8);
        result[9] = toBytes(pwd1)[0];
        result[10] = toBytes(pwd2)[0];
        result[11] = toBytes(pwd3)[0];
        result[12] = toBytes(pwd4)[0];
        result[13] = (byte) 0xff;
        result[14] = (byte) 0xff;
        result[15] = (byte) 0xff;
        result[16] = (byte) 0xff;
        result[17] = (byte) 0xff;
        result[18] = (byte) 0xff;
        return result;
    }

    //调用方法，补空
    private static String changeLength(String str) {
        int size = str.length();
        String temp = str;
        for (int i = 0; i < 8-size; i++) {
            temp +="f";
        }
        return temp;
    }

    private static String changeLength2(String str) {
        int size = str.length();
        String temp = str;
        for (int i = 0; i < 10-size; i++) {
            temp +="f";
        }
        return temp;
    }

    //调用方法，补空
    private static String changeLength1(String str) {
        int size = str.length();
        String temp = str;
        for (int i = 0; i < 6-size; i++) {
            temp = "0"+temp;
        }
        return temp;
    }


    public static byte[] setClearPwdByte(String value){
        byte[] result = new byte[19];
        result[0] = HEADER;
        result[2] = (byte)0x0b;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_CLEARPWD;
        result[7] = (byte) 0x09;
        result[8] = (byte) 0xff;
        if(value.length() < 8){
            value = changeLength(value);
        }
        String pwd1 = value.substring(0,2);
        String pwd2 = value.substring(2,4);
        String pwd3 = value.substring(4,6);
        String pwd4 = value.substring(6,8);
        result[9] = toBytes(pwd1)[0];
        result[10] = toBytes(pwd2)[0];
        result[11] = toBytes(pwd3)[0];
        result[12] = toBytes(pwd4)[0];
        result[13] = (byte) 0xff;
        result[14] = (byte) 0xff;
        result[15] = (byte) 0xff;
        result[16] = (byte) 0xff;
        result[17] = (byte) 0xff;
        result[18] = (byte) 0xff;
        return result;
    }


    public static byte[] setAllPwdByte(String value){
        byte[] result = new byte[19];
        result[0] = HEADER;
        result[2] = (byte)0x0d;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_SETALLPWD;
        result[7] = (byte) 0x0b;
        result[8] = (byte) 0xff;
        if(value.length() < 8){
            value = changeLength(value);
        }
        String pwd1 = value.substring(0,2);
        String pwd2 = value.substring(2,4);
        String pwd3 = value.substring(4,6);
        String pwd4 = value.substring(6,8);
        result[9] = toBytes(pwd1)[0];
        result[10] = toBytes(pwd2)[0];
        result[11] = toBytes(pwd3)[0];
        result[12] = toBytes(pwd4)[0];
        result[13] = (byte) 0xff;
        result[14] = (byte) 0xff;
        result[15] = (byte) 0xff;
        result[16] = (byte) 0xff;
        result[17] = (byte) 0xff;
        result[18] = (byte) 0xff;
        return result;
    }


    public static byte[] setInterimPwdByte(int seq,String value,long startTime,long endTime){
        byte[] result = new byte[19];
        result[0] = HEADER;
        result[2] = (byte)0x0d;
        result[4] = intToByte(seq);
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_SETINTERIMPWD;
        result[7] = (byte) 0x08;
        result[8] = (byte) 0xff;
        if(value.length() < 8){
            value = changeLength(value);
        }
        String pwd1 = value.substring(0,2);
        String pwd2 = value.substring(2,4);
        String pwd3 = value.substring(4,6);
        String pwd4 = value.substring(6,8);
        result[9] = toBytes(pwd1)[0];
        result[10] = toBytes(pwd2)[0];
        result[11] = toBytes(pwd3)[0];
        result[12] = toBytes(pwd4)[0];

        String start = changeLength1(numToHex8((int)startTime)+"");
        String start0 = start.substring(0,2);
        String start1 = start.substring(2,4);
        String start2 = start.substring(4,6);
        result[13] = toBytes(start0)[0];
        result[14] = toBytes(start1)[0];
        result[15] = toBytes(start2)[0];

        String end = changeLength1(numToHex8((int)endTime)+"");
        String end0 = end.substring(0,2);
        String end1 = end.substring(2,4);
        String end2 = end.substring(4,6);
        result[16] = toBytes(end0)[0];
        result[17] = toBytes(end1)[0];
        result[18] = toBytes(end2)[0];
        return result;
    }





    public static byte[] setPwdByte(int seq,byte[] content){
        byte[] result = new byte[8+content.length];
        result[0] = HEADER;
        result[2] = intToByte(content.length+2);
        result[4] = intToByte(seq);
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_SETPWD;
        result[7] = intToByte(content.length);
        System.arraycopy(content,0,result,8,content.length);
        return result;
    }


    public static byte[] openDoor(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = AUTHOR_COMMAND_ID;
        result[6] = KEY_OPENDOOR;
        result[7] = (byte) 0x00;
        return result;
    }


    public static byte[] clearPwd(byte id){
        byte[] result = new byte[9];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_OPENDOOR;
        result[7] = (byte) 0x01;
        result[8] = id;
        return result;
    }


    public static byte[] readCard(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_READCARD;
        result[7] = (byte) 0x00;
        return result;
    }


    public static byte[] addCard(boolean isForever,byte b1,byte b2,byte b3,byte b4,long startTime,long endTime){
        byte[] result ;
        if(isForever){
            result = new byte[19];
            result[2] = (byte)0x0d;
        }else{
            result = new byte[19];
            result[2] = (byte)0x0d;
        }
        result[0] = HEADER;

        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_ADDCARD;
        if(isForever){
            result[7] = (byte) 0x0b;
        }else{
            result[7] = (byte) 0x0b;
        }
        result[8] = (byte)0xff;
//        if(pwd.length() < 6){
//            pwd = changeLength(pwd);
//        }
//
//        String pwd1 = pwd.substring(0,2);
//        String pwd2 = pwd.substring(2,4);
//        String pwd3 = pwd.substring(4,6);
//        result[9] = toBytes(pwd1)[0];
//        result[10] = toBytes(pwd2)[0];
//        result[11] = toBytes(pwd3)[0];

        result[9] = b1;
        result[10] = b2;
        result[11] = b3;
        result[12] = b4;

        if(isForever){
            result[13] = (byte) 0xff;
            result[14] = (byte) 0xff;
            result[15] = (byte) 0xff;
            result[16] = (byte) 0xff;
            result[17] = (byte) 0xff;
            result[18] = (byte) 0xff;
        }else{
            String start = changeLength1(numToHex8((int)startTime)+"");
            String start0 = start.substring(0,2);
            String start1 = start.substring(2,4);
            String start2 = start.substring(4,6);
            result[13] = toBytes(start0)[0];
            result[14] = toBytes(start1)[0];
            result[15] = toBytes(start2)[0];

            String end = changeLength1(numToHex8((int)endTime)+"");
            String end0 = end.substring(0,2);
            String end1 = end.substring(2,4);
            String end2 = end.substring(4,6);
            result[16] = toBytes(end0)[0];
            result[17] = toBytes(end1)[0];
            result[18] = toBytes(end2)[0];
        }
        //result[9] = (byte)0x;
        return result;
    }

    public static byte[] addOnePWd(boolean isForever,String pwd,long startTime,long endTime){
        byte[] result ;
        if(isForever){
            result = new byte[20];
            result[2] = (byte)0x0e;
        }else{
            result = new byte[20];
            result[2] = (byte)0x0e;
        }
        result[0] = HEADER;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_ONESETPWD;
        if(isForever){
            result[7] = (byte) 0x0c;
        }else{
            result[7] = intToByte(12);
        }
        result[8] = (byte)0xff;
        if(pwd.length() < 10){
            pwd = changeLength2(pwd);
        }
        String pwd1 = pwd.substring(0,2);
        String pwd2 = pwd.substring(2,4);
        String pwd3 = pwd.substring(4,6);
        String pwd4 = pwd.substring(6,8);
        String pwd5 = pwd.substring(8,10);
        result[9] = toBytes(pwd1)[0];
        result[10] = toBytes(pwd2)[0];
        result[11] = toBytes(pwd3)[0];
        result[12] = toBytes(pwd4)[0];
        result[13] = toBytes(pwd5)[0];
        if(isForever){
            result[14] = (byte) 0xff;
            result[15] = (byte) 0xff;
            result[16] = (byte) 0xff;
            result[17] = (byte) 0xff;
            result[18] = (byte) 0xff;
            result[19] = (byte) 0xff;
        }else{
            String start = changeLength1(numToHex8((int)startTime)+"");
            String start0 = start.substring(0,2);
            String start1 = start.substring(2,4);
            String start2 = start.substring(4,6);
            result[14] = toBytes(start0)[0];
            result[15] = toBytes(start1)[0];
            result[16] = toBytes(start2)[0];

            String end = changeLength1(numToHex8((int)endTime)+"");
            String end0 = end.substring(0,2);
            String end1 = end.substring(2,4);
            String end2 = end.substring(4,6);
            result[17] = toBytes(end0)[0];
            result[18] = toBytes(end1)[0];
            result[19] = toBytes(end2)[0];
        }
        return result;
    }


    public static byte[] delCard(int id){
        byte[] result = new byte[9];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_DELCARD;
        result[7] = (byte) 0x01;
        result[8] = intToByte(id);
        return result;
    }

    public static byte[] delPwd(int id){
        byte[] result = new byte[9];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_CLEARALLPWD;
        result[7] = (byte) 0x01;
        result[8] = intToByte(id);
        return result;
    }

    //dd00020201021900
    public static byte[] setClear(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEYCLEARALL;
        result[7] = (byte) 0x00;
        return result;
    }

    //dd00020201010100  返回01和41
    public static byte[] getUpdate(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[4] = SEQ;
        result[5] = FIRMWARE_UPDATE_CMD_ID;
        result[6] = KEY_ENTER_FIRMWARE_UPDATE_MODE;
        result[7] = (byte) 0x01;
        return result;
    }

    public static byte[] getPower(){
        byte[] result = new byte[8];
        result[0] = HEADER;
        result[2] = (byte)0x02;
        result[3] = (byte)0x02;
        result[4] = SEQ;
        result[5] = SET_CONFIG_COMMAND_ID;
        result[6] = KEY_GETPOWER;
        result[7] = (byte) 0x00;
        return result;
    }

    //13713455018

    public static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {

            sbu.append((int)chars[i]);
        }
        return sbu.toString();
    }

    public static byte intToByte(int C){
        //String d = Integer.toHexString(C).toLowerCase();
        byte[] ddR = intToByteArray1(C);
        byte data = ddR[3];
        return data;
    }

    public static byte[] intToByteArray1(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    public static String bytesToString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];

            sb.append(hexChars[i * 2]);
            sb.append(hexChars[i * 2 + 1]);
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String bytesToString1(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];

            sb.append(hexChars[i * 2]);
            sb.append(hexChars[i * 2 + 1]);
        }
        return sb.toString();
    }

    public static String numToHex8(int b) {
        return String.format("%02x", b);//2表示需要两个16进行数
    }


    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
}
