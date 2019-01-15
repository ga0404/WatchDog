package com.dodoiot.lockapp.model;

import java.util.List;

public class LockPwdList extends BaseBean {
    public List<LockPassWord> pwdlist;

    public List<LockPassWord> getPwdlist() {
        return pwdlist;
    }

    public void setPwdlist(List<LockPassWord> pwdlist) {
        this.pwdlist = pwdlist;
    }
}
