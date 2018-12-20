package com.dodoiot.lockapp.model;

import java.util.List;

public class DeviceListBean extends GroupBean {
    public List<DeviceBean> devList;

    public List<DeviceBean> getDevList() {
        return devList;
    }

    public void setDevList(List<DeviceBean> devList) {
        this.devList = devList;
    }
}
