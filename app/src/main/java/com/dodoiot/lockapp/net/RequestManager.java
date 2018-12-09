package com.dodoiot.lockapp.net;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.util.NetworkUtils;


import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/14.
 */
public class RequestManager {

    private static RequestQueue mRequestQueue;
    private static final String TAG = "RequestManager";

    private RequestManager() {
        // no instances
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static boolean isTruncate = true;
    public static String cookie;

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static void request(final String url, JSONObject params,
                               final IResponseParser parser, Object tag, boolean isPost) {
//        Log.i(TAG, "reqeust:" + BaseConfig.SERVERPATH + url + ",\nparams="
//                + (params != null ? params.toString() : ""));
        JsonObjectRequest req = new MyJsonRequest(isPost ? Request.Method.POST
                : Request.Method.GET, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parser.parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                parser.parseError(error);
            }
        });
        RequestManager.addRequest(req, tag);
    }

    public static void requestString(Activity activity, final String url,
                                     final Map<String, String> params, final IResponseParser parser,
                                     final Object tag, boolean isPost) {


        if (NetworkUtils.getNetworkState(activity) == NetworkUtils.NONE) {
            if(activity != null){

                Toast.makeText(activity, R.string.tips_nointernet,Toast.LENGTH_LONG).show();
                parser.parseDialog(1);
            }
            return;
        }


        parser.parseDialog(0);
        HTTPSTrustManager.allowAllSSL();
        StringRequest request = new MyStringRequest(isPost ? Request.Method.POST
                : Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String string) {
                parser.parseResponse(string);
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                parser.parseError(error);
            }

        }) {

//            public Map<String, String> mHeaders = new HashMap<String, String>();
//
//            public void setCookie() {
//                mHeaders.put("accept", "*/*");
//                mHeaders.put("connection", "Keep-Alive");
//                if (null != cookie && !cookie.equals(""))
//                    mHeaders.put("Cookie", cookie);
//            }
//
//            public void synCookies(Context context, String url, String cookies) {
//                CookieSyncManager.createInstance(context);
//                CookieManager cookieManager = CookieManager.getInstance();
//                cookieManager.setAcceptCookie(true);
//                cookieManager.removeSessionCookie();// 移除
//                cookieManager.setCookie(url, cookies);// 指定要修改的cookies
//                CookieSyncManager.getInstance().sync();
//
//            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                setCookie();
//                return mHeaders;
//            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    // mHeader = response.headers.toString();
                    // L.e("LOG", "get headers in parseNetworkResponse "
                    // + response.headers.toString());
                    // 使用正则表达式从reponse的头中提取cookie内容的子串
                    Pattern pattern = Pattern.compile("Set-Cookie.*?;");
                    Matcher m = pattern.matcher(response.headers.toString());
                    if (m.find()) {
                        // cookie = m.group();
                        // L.e("LOG","cookie from server "+ cookie);
                        // 去掉cookie末尾的分号
                        if (isTruncate) {
                            isTruncate = false;
                            cookie = m.group().substring(11, m.group().length() - 1);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return super.parseNetworkResponse(response);
            }
        };
        if (BaseConfig.isDebug)
            request.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        request.setShouldCache(false);
        RequestManager.addRequest(request, tag);
    }


//    public static void requestImage(String url, final IResponseImageParser parser,Object tag) {
//        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                parser.parseResponse(bitmap);
//            }
//        }, 0, 0, Bitmap.Config.ARGB_8888, // 图片的颜色属性
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        parser.parseError(volleyError);
//                    }
//                }){
//            @Override
//            protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
//                Map<String, String> responseHeaders = response.headers;
//                try {
//                    String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers));
//                    // mHeader = response.headers.toString();
//                    // L.e("LOG", "get headers in parseNetworkResponse "
//                    // + response.headers.toString());
//                    // 使用正则表达式从reponse的头中提取cookie内容的子串
//                    Pattern pattern = Pattern.compile("Set-Cookie.*?;");
//                    Matcher m = pattern.matcher(response.headers.toString());
//                    if (m.find()) {
//                        // cookie = m.group();
//                        // L.e("LOG","cookie from server "+ cookie);
//                        // 去掉cookie末尾的分号
//                        if (isTruncate) {
//                            isTruncate = false;
//                            cookie = m.group().substring(11, m.group().length() - 1);
//                            Log.e("dfc", "<-------------getimage----------------------------------------------->" + cookie);
//                        }
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                return super.parseNetworkResponse(response);
//            }
//        };
//        imageRequest.setShouldCache(false);
//        RequestManager.addRequest(imageRequest, tag);
//    }

    static class MyJsonRequest extends JsonObjectRequest {

        public MyJsonRequest(int method, String url, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Charset", "UTF-8");
            headers.put("Content-Type", "application/json");
            return headers;
        }
    }


    static class MyStringRequest extends StringRequest {

        public MyStringRequest(int method, String url,
                               Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
            // TODO Auto-generated constructor stub
        }

    }



    /*
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 *
	 * @param url Service net address
	 *
	 * @param params text content
	 *
	 * @param files pictures
	 *
	 * @return String result of Service response
	 *
	 * @throws IOException
	 */
    public static void connectionFilePostMsg(String url,
                                             Map<String, String> params, Map<String, File> files,
                                             final IResponseParser callBack) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        HTTPSTrustManager.allowAllSSL();
        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时�?
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓�?
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参�?
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发�?文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\""
                        + file.getKey() + "\"; filename=\""
                        + file.getValue().getName() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应�?
        int res = conn.getResponseCode();

        InputStream in = conn.getInputStream();
        StringBuilder sb2 = new StringBuilder();
        if (res == 200) {
            int ch;
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        } else {

            if (callBack != null)
                callBack.parseResponse("服务器响应失败");
        }
        outStream.close();
        conn.disconnect();
        System.out.println(new String(sb2.toString().getBytes("ISO-8859-1"),
                "UTF-8"));
        callBack.parseResponse(new String(sb2.toString().getBytes("ISO-8859-1"),
                "UTF-8"));
    }

}

