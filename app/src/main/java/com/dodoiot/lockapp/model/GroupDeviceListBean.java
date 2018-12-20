package com.dodoiot.lockapp.model;

import java.util.List;

public class GroupDeviceListBean extends BaseBean {
    public List<DeviceListBean> grouplist;

    public List<DeviceListBean> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<DeviceListBean> grouplist) {
        this.grouplist = grouplist;
    }
}
