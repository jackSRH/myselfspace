package com.mailian.core.converter;

import com.mailian.core.util.ThreadLocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/9/10
 * @Description:
 */
public class DateConverter implements Converter<String,Date> {
    private static final Logger log = LoggerFactory.getLogger(DateConverter.class);

    @Override
    public Date convert(String s) {
        try {
            return ThreadLocalDateUtil.parse(s);
        } catch (ParseException e) {
            log.error("日期:{} 转换异常!",s);
        }
        return null;
    }
}
