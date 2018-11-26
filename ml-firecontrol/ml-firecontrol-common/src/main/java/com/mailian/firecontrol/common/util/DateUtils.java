package com.mailian.firecontrol.common.util;

import java.util.Date;

public class DateUtils {

     public static long MILLISECONDS_HOUR = 1000 * 60 * 60;// 一小时的毫秒数


    /**
     * 两个时间点相隔几个小时
     * @param startDate
     * @param endDate
     * @return
     *
     */
    public static int getHourBetweenTwoDate(Date startDate, Date endDate){

        return  (int)((endDate.getTime()-startDate.getTime())/MILLISECONDS_HOUR);
    }

    /**
     * 某个时间点距今几个小时
     * @param date
     * @return
     *
     */
    public static int getHourFromNow(Date date){

        return getHourBetweenTwoDate(date ,new Date());
    }

}
