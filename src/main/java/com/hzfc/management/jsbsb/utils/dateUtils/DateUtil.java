package com.hzfc.management.jsbsb.utils.dateUtils;

/**
 * @Author yxx
 * @Date 2021/4/27 14:21
 */

import java.time.*;
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
    public static LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }


    //日期和字符串之间的转换 DateUtil.format(date, "yyyy-MM-dd")
    public static String format(Date date, String type) {
        LocalDate localDate = toLocalDate(date);
        //建一个DaeTimeFormatter类的对象
        //里面传入想要的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(type);
        //用当前时间的对象调用format方法
        String format = localDate.format(formatter);
        return format;
    }


    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     */
    public static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
