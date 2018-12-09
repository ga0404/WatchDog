package com.dodoiot.lockapp.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.net.URL;

public class CommonUtil {
    private static final String TAG = CommonUtil.class.getSimpleName();

    /**
     * 将String转成html处理
     *
     * @param source
     * @return
     */
    public static Spanned formatHtml(String source) {
        try {
            return Html.fromHtml(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int formatStringToInt(String data) {
        try {
            return Integer.parseInt(data);
        } catch (Exception e) {
//            DebugLog.d("formatStringToInt error. format = " + data);
        }
        return 0;
    }

    /**
     * String安全转换Double
     *
     * @param data String
     * @return Double
     */
    public static double formatStringToDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception e) {
//            DebugLog.d("formatStringToInt error. format = " + data);
        }
        return 0.0;
    }

    public static long formatStringToLong(String data) {
        try {
            return Long.parseLong(data);
        } catch (Exception e) {
//            DebugLog.d("formatStringToInt error. format = " + data);
        }
        return 0;
    }

    public static String formatDoubleToString(double data) {
        try {
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(data));
            bigDecimal.setScale(6);
            return bigDecimal.toString();
        } catch (Exception e) {
//            DebugLog.d("formatDoubleToString error. format = " + data);
        }
        return "0";
    }

    public static String formatByteToString(byte[] buffer) {
        if (null == buffer) {
            return null;
        }
        try {
            return new String(buffer, "utf-8");
        } catch (Exception e) {
//            DebugLog.d("formatByteToString error.");
        }
        return null;
    }

    /**
     * 格式化,保留小数点后{#length}位
     *
     * @param data
     */
    public static String formatDataLength(String data, int length) {
        BigDecimal bd = new BigDecimal(data);
        bd = bd.setScale(length, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    /**
     * 复制字符串
     *
     * @param content 字符串
     */
    public static void copyContentIntoClipboard(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
//        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData textCd = ClipData.newPlainText("invitationCode", content);
//        clipboard.setPrimaryClip(textCd);
//        ToastUtils.showToast(context, R.string.common_copy_suc);
    }

    /**
     * 获取机器识别码 imei
     *
     * @param context 上下文参数
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @param context 上下文参数
     * @return
     */
    public static String getPackageVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文参数
     * @return
     */
    public static String getPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Spanned fromHtml(String souce) {
        try {
            Spanned spanned = Html.fromHtml(souce, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String s) {
//                    DebugLog.e("Html getDrawable s = " + s);
                    Drawable drawable = null;
                    URL url;
                    try {
                        url = new URL(s);
                        drawable = Drawable.createFromStream(url.openStream(), "");  //获取网路图片
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                    if (null == drawable) {
                        return null;
                    }
                    // Important
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                            .getIntrinsicHeight());
                    return drawable;
                }
            }, null);
            return spanned;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取系统SDK版本、OS版本等信息
     *
     * @param context 上下文参数
     * @return
     */
    public static String getSystemVersion(Context context) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Build.VERSION.SDK_INT);//SDK 版本号
        buffer.append("+");
        buffer.append(Build.VERSION.RELEASE);//OS 版本号
        return buffer.toString();
    }

    /**
     * 获取mac IP地址
     *
     * @param context 上下文参数
     * @return
     */
    public static String getMacIP(Context context) {
//        try {
//            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            if (wifiInfo.getMacAddress() != null) {
//                return wifiInfo.getMacAddress();
//            } else {
//                return "";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return "";
    }

    /**
     * 设置壁纸
     *
     * @param context
     * @param bitmap
     */
    public static void setWallPaper(Context context, Bitmap bitmap) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
