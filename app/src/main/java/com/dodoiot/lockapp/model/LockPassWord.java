package com.dodoiot.lockapp.model;

import java.io.Serializable;

public class LockPassWord implements Serializable {

    public String id;
    public String deviceId;
    public String createtime;
    public String createbyId;
    public String remark;
    public String fromTime;
    public String toTime;
    public String value;
    public String enable;
    public String usertimes;
    public String name;
    public String type;
    public String innerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatebyId() {
        return createbyId;
    }

    public void setCreatebyId(String createbyId) {
        this.createbyId = createbyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getUsertimes() {
        return usertimes;
    }

    public void setUsertimes(String usertimes) {
        this.usertimes = usertimes;
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

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }
}
