package com.dodoiot.lockapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {
    public static Map<String, Object> getMap(Object obj) {
        return JsonUtils.getMap(JsonUtils.toJson(obj));
    }

    public static Map<String, Object> getMap(String jsonString) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static Object jsonToObj(String json,
                                   @SuppressWarnings("rawtypes") Class clazz) {
        return JsonUtils.getGson().fromJson(json, clazz);
    }


    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson GSON = gsonBuilder.create();
        return GSON;
    }


    public static String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    public static Object jsonToObj(JSONObject json, Class clazz) {
        return JsonUtils.getGson().fromJson(json.toString(), clazz);
    }


    public static String[] JSONArrayToStringArray(JSONArray jsonArray) {
        int length = jsonArray.length();
        String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            arr[i] = jsonArray.optString(i, "");
        }
        return arr;
    }
}
