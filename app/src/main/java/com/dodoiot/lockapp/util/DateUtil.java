package com.dodoiot.lockapp.util;

import com.dodoiot.lockapp.util.CommonUtil;
import com.dodoiot.lockapp.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (StringUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }


    public static String getHourDate(){return  getDate("yyyy-MM-dd");}

    public static String getMinuteDate(){return  getDate("yyyy-MM-dd HH:mm");}

    private static SimpleDateFormat formatBuilder;

    /**
     * 获取日期时间
     *
     * @param format String
     * @return String
     */
    public static String getDate(String format) {
        formatBuilder = new SimpleDateFormat(format);
        return formatBuilder.format(new Date());
    }

    /**
     * unix时间戳转换为dateFormat
     *
     * @param beginDate
     * @return
     */
    public static String timestampToDate1(String beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sd = sdf.format(new Date(CommonUtil.formatStringToLong(beginDate)));
        return sd;
    }

    public static String getDateStringByTimeMillis(long timeMillis) {
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(gc.getTime());
    }

    public static String getDateStringByTimeMillis1(long timeMillis) {
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(gc.getTime());
    }
    public static String getDateStringByTimeMillis2(long timeMillis) {
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(gc.getTime());
    }


    public static String getDataFormat(String dateStr){
        SimpleDateFormat format =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Long time=new Long(dateStr);
        String d = format.format(time);
        return d;
    }

    /**
     * @param date   时间
     * @param format 需要格式化的格式模板
     */
    public static long getTimeMillis(String date, String format) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat(format).parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }

    /**
     * 获取当毫秒数
     *
     * @param date 需要转换的时间（格式yyyy/MM/dd HH:mm:ss）
     * @return 返回long
     */
    public static long getTimeMillis1(String date) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }


    /**
     * 获取当毫秒数
     *
     * @param date 需要转换的时间（格式yyyy/MM/dd HH:mm:ss）
     * @return 返回long
     */
    public static long getTimeMillis(String date) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }


    /**
     * 获取当毫秒数
     *
     * @param date 需要转换的时间（格式yyyy/MM/dd HH:mm:ss）
     * @return 返回long
     */
    public static long getTimeMillis2(String date) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }
    /**
     * 获取当前时间毫秒值
     *
     * @return long
     */
    public static long getCurrentTimeMills() {
        return new Date().getTime();
    }
}
