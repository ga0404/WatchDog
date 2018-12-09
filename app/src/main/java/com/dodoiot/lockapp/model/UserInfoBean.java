package com.dodoiot.lockapp.model;

import java.io.Serializable;

public class UserInfoBean implements Serializable {
    private String userid;
    private String username;
    private String logintime;
    private String nickname;
    private String mobile;
    private String imageurl;
    private String createtime;
    private String sex;


    //{"code":"0","msg":"OK","token":"VBKRWLHLDLWSVPGAAKHQFSNKGPUL964D","userinfo":{"createtime":"2018-08-09 19:18:52","imageuri":"","logintime":"2018-11-13 15:05:46","mobile":"18680677626","nickname":"18680677626","sex":"","userid":"964","username":"18680677626"}}


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
