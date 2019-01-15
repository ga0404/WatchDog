package com.dodoiot.lockapp.model;

import java.io.Serializable;
import java.util.List;

public class LockMsgBean extends BaseBean  {

    private String encKey;//加密密匙
    private String commKey;//通讯密码
    private String time;//时间
    private List<PwdListBean> pwdList;


    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getCommKey() {
        return commKey;
    }

    public void setCommKey(String commKey) {
        this.commKey = commKey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<PwdListBean> getPwdList() {
        return pwdList;
    }

    public void setPwdList(List<PwdListBean> pwdList) {
        this.pwdList = pwdList;
    }

    public static class PwdListBean implements Serializable {
        /**
         * type : 0
         * value : 73548477
         */

        private int type;//0管理员密码 1清除密码 2永久密码 3临时密码
        private String value;
        private String encKey;//加密密匙
        private String commKey;//通讯密码
        private String time;//时间

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getEncKey() {
            return encKey;
        }

        public void setEncKey(String encKey) {
            this.encKey = encKey;
        }

        public String getCommKey() {
            return commKey;
        }

        public void setCommKey(String commKey) {
            this.commKey = commKey;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
