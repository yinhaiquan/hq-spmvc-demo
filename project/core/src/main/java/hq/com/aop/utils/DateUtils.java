package hq.com.aop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.*;

/**
 * 日期工具类
 * <p>
 * Created by Yinhaiquan on 2017/5/10.
 */
public final class DateUtils {
    /**
     * 时区：中国标准时间 东八区
     */
    private static String TIMEZONE = "GMT+8";
    /**
     * yyyyMMdd
     */
    public static String YYYYMMDD = "yyyyMMdd";

    /**
     * yyyy-MM-dd
     */
    public static String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * yyyyMMdd HH:mm:ss
     */
    public static String YYYYMMDD_HH_MM_SS = "yyyyMMdd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss:sss
     */
    public static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";

    /**
     * yyyyMMddHHmmss
     */
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 设置时区
     */
    static {
        initTime();
        System.out.println(StringUtils.format("设置时区:[{0}]", TIMEZONE));
    }

    /**
     * Date日期转换String类型
     *
     * @param date   目标日期
     * @param format 转换日期格式
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (StringUtils.isEmpty(date)){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * String日期转换Date类型
     *
     * @param date   目标日期String
     * @param format 转换日期格式
     * @return
     */
    public static Date stringToDate(String date, String format) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加天数
     *
     * @param date 目标日期
     * @param days 天数
     * @return
     */
    public static Date addDay(Date date, int days) {
        return new DateTime(date).plusDays(days).toDate();
    }

    /**
     * 增加月数
     *
     * @param date   目标日期
     * @param months 天数
     * @return
     */
    public static Date addMonth(Date date, int months) {
        return new DateTime(date).plusMonths(months).toDate();
    }

    /**
     * 计算两个日期的日差值
     *
     * @param date1
     * @param date2
     * @return
     * @describle date1、date2类型支持String、java.util.Date、org.joda.time.DateTime类型
     */
    public static int datesBetweenDays(Object date1, Object date2) {
        DateTime dateTime1 = formatObject(date1), dateTime2 = formatObject(date2);
        if (null != dateTime1 && null != dateTime2) {
            return Math.abs(Days.daysBetween(dateTime1, dateTime2).getDays());
        }
        return -1;
    }

    /**
     * 计算两个日期的月差值
     *
     * @param date1
     * @param date2
     * @return
     * @describle date1、date2类型支持String、java.util.Date、org.joda.time.DateTime类型
     */
    public static int datesBetweenMonths(Object date1, Object date2) {
        DateTime dateTime1 = formatObject(date1), dateTime2 = formatObject(date2);
        if (null != dateTime1 && null != dateTime2) {
            return Math.abs(Months.monthsBetween(dateTime1, dateTime2).getMonths());
        }
        return -1;
    }

    /**
     * 计算两个日期的年差值
     *
     * @param date1
     * @param date2
     * @return
     * @describle date1、date2类型支持String、java.util.Date、org.joda.time.DateTime类型
     */
    public static int datesBetweenYears(Object date1, Object date2) {
        DateTime dateTime1 = formatObject(date1), dateTime2 = formatObject(date2);
        if (null != dateTime1 && null != dateTime2) {
            return Math.abs(Years.yearsBetween(dateTime1, dateTime2).getYears());
        }
        return -1;
    }

    /**
     * 获取当前月第一天
     *
     * @param format 日期转换格式
     * @return String
     */
    public static String getMonthStartDate(String format) {
        return dateToString(getMonthStartDate(), format);
    }

    /**
     * 获取当前月第一天
     *
     * @return Date
     */
    public static Date getMonthStartDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当前周第一天
     *
     * @param format 日期转换格式
     * @return String
     */
    public static String getFirstDayOfWeek(String format) {
        return dateToString(getFirstDayOfWeek(), format);
    }

    /**
     * 获取当前周第一天
     *
     * @return Date
     */
    public static Date getFirstDayOfWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当前季度第一天
     *
     * @param format 日期转换格式
     * @return String
     */
    public static String getFirstDayOfSeason(String format) {
        return dateToString(getFirstDayOfSeason(), format);
    }

    /**
     * 获取当前季度第一天
     *
     * @return Date
     */
    public static Date getFirstDayOfSeason() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        switch (currentMonth) {
            case 1:
            case 2:
            case 3:
                c.set(Calendar.MONTH, 1);
                break;
            case 4:
            case 5:
            case 6:
                c.set(Calendar.MONTH, 3);
                break;
            case 7:
            case 8:
            case 9:
                c.set(Calendar.MONTH, 4);
                break;
            case 10:
            case 11:
            case 12:
                c.set(Calendar.MONTH, 9);
                break;
        }
        c.set(Calendar.DATE, 1);
        return stringToDate(dateToString(c.getTime(), YYYY_MM_DD) + " 00:00:00", YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取本年第一天
     *
     * @param format 日期转换格式
     * @return String
     */
    public static String getFirstDayOfYear(String format) {
        return dateToString(getFirstDayOfYear(), format);
    }

    /**
     * 获取本年第一天
     *
     * @return Date
     */
    public static Date getFirstDayOfYear() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取当月最后一天日期
     *
     * @param format 日期转换格式
     * @return
     */
    public static String getLastDayOfMonth(String format) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateToString(c.getTime(), format);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getNow(String format) {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static void initTime() {
        TimeZone time = TimeZone.getTimeZone(TIMEZONE);
        TimeZone.setDefault(time);
    }

    private static DateTime formatObject(Object date) {
        DateTime dateTime = null;
        if (date instanceof String) {
            dateTime = new DateTime(stringToDate((String) date, YYYYMMDD_HH_MM_SS));
        } else if (date instanceof Date) {
            dateTime = new DateTime(date);
        } else if (date instanceof DateTime) {
            dateTime = (DateTime) date;
        }
        return dateTime;
    }

    public static void main(String[] args) {
        System.out.println(dateToString(new Date(), YYYY_MM_DD_HH_MM_SS_SSS));
        System.out.println(getFirstDayOfYear());
        System.out.println(getFirstDayOfYear(YYYYMMDDHHMMSS));
//		System.out.println(getFirstDayOfSeason());
//		System.out.println(getFirstDayOfSeason(YYYYMMDDHHMMSS));
//		System.out.println(stringToDate("2013-12-23 00:00:00",YYYY_MM_DD_HH_MM_SS));
//		System.out.println(getNow(YYYYMMDDHHMMSS));
//		System.out.println(getMonthStartDate(YYYYMMDDHHMMSS));
//		System.out.println(getFirstDayOfWeek());
        // DateTime dateTime = new DateTime(new Date());
        // DateTime date = dateTime;
        // date = date.plusDays(67);
        //
        // System.out.println(datesBetweenDays(date,dateTime));
        // System.out.println(datesBetweenMonths(date,dateTime));
        // System.out.println(datesBetweenYears(date,dateTime));
    }
}
