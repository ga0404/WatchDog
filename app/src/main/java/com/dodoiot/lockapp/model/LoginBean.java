package com.dodoiot.lockapp.model;

public class LoginBean extends BaseBean {

    private String token;
    private UserInfoBean userinfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfoBean userinfo) {
        this.userinfo = userinfo;
    }
}
