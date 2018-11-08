package com.mailian.firecontrol.framework.util;

import com.mailian.core.util.DateUtil;
import com.mailian.firecontrol.dto.DayTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/11/8
 * @Description:
 */
public class ConvertDateUtil {
    /**
     * 获取某个时间段内所有的小时
     * @param dBegin
     * @param dEnd
     * @return
     *
     */
    public static List<DayTime> getHoursBetween(Date dBegin, Date dEnd){
        List<DayTime> lDate = new ArrayList<>();
        SimpleDateFormat sd = new SimpleDateFormat(DateUtil.DATE_FORMAT_FOR_YMDH);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())){
            DayTime dayTime = new DayTime();
            dayTime.setShowTime(sd.format(calBegin.getTime()));
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.HOUR_OF_DAY, 1);
            dayTime.setRealTime(sd.format(calBegin.getTime()));

            lDate.add(dayTime);
        }
        return lDate;
    }

    /**
     * 获取某个时间段内所有的日
     * @param dBegin
     * @param dEnd
     * @return
     *
     */
    public static List<DayTime> getDaysBetween(Date dBegin, Date dEnd){
        List<DayTime> lDate = new ArrayList<>();
        SimpleDateFormat sd = new SimpleDateFormat(DateUtil.DATE_FORMAT_FOR_YMD);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);

        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            DayTime dayTime = new DayTime();
            dayTime.setShowTime(sd.format(calBegin.getTime()));
            dayTime.setRealTime(sd.format(calBegin.getTime()));
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(dayTime);
        }
        return lDate;
    }


    /**
     * 获取某个时间段内所有的月
     * @param dBegin
     * @param dEnd
     * @return
     *
     */
    public static List<DayTime> getMonthsBetween(Date dBegin, Date dEnd){
        List<DayTime> lDate = new ArrayList<>();
        SimpleDateFormat sd = new SimpleDateFormat(DateUtil.DATE_FORMAT_FOR_YM);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);

        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            DayTime dayTime = new DayTime();
            dayTime.setShowTime(sd.format(calBegin.getTime()));
            dayTime.setRealTime(sd.format(calBegin.getTime()));
            calBegin.add(Calendar.MONTH, 1);
            lDate.add(dayTime);
        }
        return lDate;
    }
}
