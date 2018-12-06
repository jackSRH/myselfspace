package com.mailian.firecontrol.common.util;

import java.util.Date;

public class DateUtils {

     public static long MILLISECONDS_HOUR = 1000 * 60 * 60;// 一小时的毫秒数
    public static long MILLISECONDS_MINUTE = 1000 * 60 ;// 一分钟的毫秒数

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


    /**
     * 两个时间点相隔几小时几分钟
     * @param startDate
     * @param endDate
     * @return
     *
     */
    public static String getHMBetweenTwoDate(Date startDate, Date endDate){
        long diffTime = endDate.getTime()-startDate.getTime();
        String hour = String.valueOf((int)(diffTime)/MILLISECONDS_HOUR) ;
        String minute = String.valueOf((int)((diffTime % MILLISECONDS_HOUR)/MILLISECONDS_MINUTE));
        return hour + "h" + minute + "m";
    }

    /**
     * 某个时间点距今几小时几分钟
     * @param date
     * @return
     *
     */
    public static String getHMFromNow(Date date){


        return getHMBetweenTwoDate(date ,new Date());
    }



}
