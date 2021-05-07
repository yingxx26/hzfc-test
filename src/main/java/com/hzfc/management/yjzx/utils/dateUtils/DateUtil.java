package com.hzfc.management.yjzx.utils.dateUtils;

/**
 * @Author yxx
 * @Date 2021/4/27 14:21
 */

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期
 */
public class DateUtil {

    /**
     * LocalDateTime -> Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * Date -> LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }


    //日期和字符串之间的转换
    public static String format(Date date,String type) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        //建一个DaeTimeFormatter类的对象
        //里面传入想要的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(type);
        //用当前时间的对象调用format方法
        String format = localDateTime.format(formatter);
        return format;
    }


}
