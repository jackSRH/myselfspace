package com.mailian.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/9/4
 * @Description:
 */
public class ThreadLocalDateUtil {
    private static final String date_format_time = "yyyy-MM-dd HH:mm:ss";
    private static final String date_format = "yyyy-MM-dd";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();
    private static ThreadLocal<DateFormat> threadTimeLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDateFormat()
    {
        DateFormat df = threadLocal.get();
        if(df==null){
            df = new SimpleDateFormat(date_format);
            threadLocal.set(df);
        }
        return df;
    }

    public static DateFormat getDateTimeFormat(){
        DateFormat timeFormat = threadTimeLocal.get();
        if(timeFormat==null){
            timeFormat = new SimpleDateFormat(date_format_time);
            threadLocal.set(timeFormat);
        }
        return timeFormat;
    }

    public static String formatDate(Date date){
        return getDateTimeFormat().format(date);
    }

    public static Date parse(String strDate) {
        try {
            if ("".equals(strDate) || strDate == null) {
                return null;
            }
            if (strDate.contains(":")) {
                return getDateTimeFormat().parse(strDate);
            }
            return getDateFormat().parse(strDate);
        }catch (ParseException e){
            return null;
        }
    }
}
