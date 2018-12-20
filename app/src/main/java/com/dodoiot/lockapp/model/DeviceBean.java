package com.dodoiot.lockapp.model;

import java.io.Serializable;

public class DeviceBean implements Serializable{

    public String id;
    public String sn;
    public String image;
    public String name;
    public String type;
    public String createTime;
    public String lastloginTime;
    public String rightSendSMS;
    public String fixedPassword;
    public String encKey;
    public String commKey;
    public String mac;
    public String groupid;
    public String valideFrom;
    public String valideTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastloginTime() {
        return lastloginTime;
    }

    public void setLastloginTime(String lastloginTime) {
        this.lastloginTime = lastloginTime;
    }

    public String getRightSendSMS() {
        return rightSendSMS;
    }

    public void setRightSendSMS(String rightSendSMS) {
        this.rightSendSMS = rightSendSMS;
    }

    public String getFixedPassword() {
        return fixedPassword;
    }

    public void setFixedPassword(String fixedPassword) {
        this.fixedPassword = fixedPassword;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getValideFrom() {
        return valideFrom;
    }

    public void setValideFrom(String valideFrom) {
        this.valideFrom = valideFrom;
    }

    public String getValideTo() {
        return valideTo;
    }

    public void setValideTo(String valideTo) {
        this.valideTo = valideTo;
    }
}
