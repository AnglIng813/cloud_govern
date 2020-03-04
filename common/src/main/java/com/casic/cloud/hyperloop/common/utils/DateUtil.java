package com.casic.cloud.hyperloop.common.utils;



import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    enum DATE_FORMAT {
        DATE_FORMAT_1("yyyy-MM-dd"),
        DATE_FORMAT_2("yyyy-MM-dd HH:mm:ss"),
        DATE_FORMAT_3("yyyyMMddHHmmss");

        private String formatStr;
        DATE_FORMAT(String formatStr) {
            this.formatStr = formatStr;
        }
        public String getFormatStr() {
            return this.formatStr;
        }
        public DateTimeFormatter getDateTimeFormatter(){
            return DateTimeFormatter.ofPattern(this.formatStr);
        }
    }

    /***
     * @Description: String -> LocalDateTime
     * @Author: LDC
     * @Date: 2019/8/1 14:24
     */
    public static LocalDateTime String2LocalDateTime(String time){
        return LocalDateTime.parse(time,DATE_FORMAT.DATE_FORMAT_2.getDateTimeFormatter());
    }

    /***
     * @Description:  LocalDateTime -> String
     * @Author: LDC
     * @Date: 2019/8/1 14:24
     */
    public static String LocalDateTime2String(LocalDateTime time){
        return time.format(DATE_FORMAT.DATE_FORMAT_2.getDateTimeFormatter());
    }

    /***
     * @Description: String -> LocalDate
     * @Author: LDC
     * @Date: 2019/8/1 14:24
     */
    public static LocalDate String2LocalDate(String time){
        return LocalDate.parse(time,DATE_FORMAT.DATE_FORMAT_1.getDateTimeFormatter());
    }

    /***
     * @Description:  LocalDate -> String
     * @Author: LDC
     * @Date: 2019/8/1 14:24
     */
    public static String LocalDate2String(LocalDate time){
        return time.format(DATE_FORMAT.DATE_FORMAT_1.getDateTimeFormatter());
    }

    /**
     * @return quarter
     * @Description: 获取当前季度
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static Integer getQuarter() {
        return (LocalDateTime.now().getMonth().getValue() / 4) + 1;
    }

    /**
     * @param date
     * @return quarter
     * @Description: 获取当前季度
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static Integer getQuarter(Date date) {
        return (convert2LocalDateTime(date).getMonth().getValue() / 4) + 1;
    }

    /**
     * @param localDateTime
     * @return quarter
     * @Description: 获取当前季度
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static Integer getQuarter(LocalDateTime localDateTime) {
        return (localDateTime.getMonth().getValue() / 4) + 1;
    }

    /***
     * @Description: 获取当前年
     * @Author: LDC
     * @Date: 2019/7/30 17:24
     * @return year
     */
    public static Integer getYear() {
        return LocalDateTime.now().getYear();
    }

    /***
     * @Description: 获取年
     * @Author: LDC
     * @Date: 2019/7/30 17:24
     * @param date
     * @return year
     */
    public static Integer getYear(Date date) {
        return convert2LocalDateTime(date).getYear();
    }

    /**
     * @param date
     * @return LocalDateTime
     * @Description: Date -> LocalDateTime
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static LocalDateTime convert2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * @param date
     * @return LocalDate
     * @Description: Date -> LocalDate
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static LocalDate convert2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @param localDateTime
     * @return Date
     * @Description: LocalDateTime -> Date
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static Date convert2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param localDate
     * @return Date
     * @Description: LocalDate -> Date
     * @Author: LDC
     * @Date: 2019/7/30 17:21
     */
    public static Date convert2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /***
     * @Description: 计算日期相差天数
     * @Author: LDC
     * @Date: 2019/8/1 14:14
     */

    public static Integer betweenDays(Date startDate, Date endDate) {
         long between = convert2LocalDate(startDate).toEpochDay() - convert2LocalDate(endDate).toEpochDay();
         return Math.abs(Math.toIntExact(between));
    }

    /***
     * @Description: 计算日期相差天数
     * @Author: LDC
     * @Date: 2019/8/1 14:14
     */

    public static Integer betweenMinutes(Date startDate, Date endDate) {
         long between = ChronoUnit.MINUTES.between(convert2LocalDateTime(startDate),convert2LocalDateTime(endDate));
         return Math.abs(Math.toIntExact(between));
    }

    /***
     * @Description: LocalDate -> Long
     * 注 该日期单位 yyyy-MM-dd 多余部分会忽略
     * @Author: LDC
     * @Date: 2019/11/14 17:11
     */
    public static Long convert2Timestample(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
    }

    /***
     * @Description: LocalDateTime -> Long
     * 注 该日期单位 yyyy-MM-dd HH:mm:ss
     * @Author: LDC
     * @Date: 2019/11/14 17:11
     */

    public static Long convert2Timestample(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /***
     * @Description: Long -> LocalDate
     * 注 该日期单位 yyyy-MM-dd 多余部分会忽略
     * @Author: LDC
     * @Date: 2019/11/14 17:11
     */
    public static LocalDate convert2LocalDate(Long timestample) {
        return Instant.ofEpochMilli(timestample).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /***
     * @Description: Long -> LocalDateTime
     * 注 该日期单位 yyyy-MM-dd HH:mm:ss
     * @Author: LDC
     * @Date: 2019/11/14 17:11
     */
    public static LocalDateTime convert2LocalDateTime(Long timestample) {
        return Instant.ofEpochMilli(timestample).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static void main(String[] args) {
        final Period between = Period.between(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10));
        System.out.println(LocalDate.now().plusDays(10));
        System.out.println(LocalDate.now().minusDays(10));
        System.out.println(LocalDate.now());
        System.out.println(convert2Timestample(LocalDate.now()));
        System.out.println(convert2Timestample(LocalDateTime.now()));
        long time = new Date().getTime();
        System.out.println(time);
        System.out.println(convert2LocalDate(time));
        System.out.println(convert2LocalDateTime(time));
    }
}
