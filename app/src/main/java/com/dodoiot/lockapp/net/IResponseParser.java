package com.dodoiot.lockapp.net;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/14.
 */
public abstract class IResponseParser {
    public abstract void parseResponse(JSONObject response);
    public abstract void parseResponse(String string);
    public abstract void parseError(VolleyError error);
    public abstract void parseDialog(int type);//0显示 1不显示
}
