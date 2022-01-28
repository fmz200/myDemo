package com.soft.mydemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeUtils {

    public static final String DATEFORMAT_01 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMAT_02 = "yyyyMMddHHmmss";
    public static final String DATEFORMAT_03 = "yyyyMMdd";
    public static final String DATEFORMAT_04 = "yyyy";
    public static final String DATEFORMAT_05 = "yyyy-mm-dd";

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrTimeString() {
        Calendar ca = Calendar.getInstance();
        Date date = ca.getTime();

        SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMAT_02);
        return fmt.format(date);
    }

    /**
     * 获取当前系统时间yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrDateString() {
        Calendar ca = Calendar.getInstance();
        Date date = ca.getTime();

        SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMAT_01);
        return fmt.format(date);
    }

    /**
     * 获取当前年月日：yyyyMMdd
     *
     * @return 当前年月日
     */
    public static String getCurrentDateString() {
        Calendar ca = Calendar.getInstance();
        Date date = ca.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_03);
        return sdf.format(date);
    }

    /**
     * 获取当前年份：yyyy
     *
     * @return 当前年份
     */
    public static String getCurrentYear() {
        Calendar ca = Calendar.getInstance();
        Date date = ca.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_04);
        return sdf.format(date);
    }

    /**
     * 获取当前系统时间yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrString() {
        return LocalDate.now().toString();
    }

    /**
     * 获取当前系统时间yyyy
     *
     * @return
     */
    public static String getCurrYearString() {
        return String.valueOf(LocalDate.now().getYear());
    }

    /**
     * 校验日期字符串是否为指定格式的
     *
     * @param str
     * @param pattern 格式
     * @return
     */
    public static boolean isValidDate(String str, String pattern) {
        boolean convertSuccess = false;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            Date d = format.parse(str);
            String strs = format.format(d);
            LocalDate.parse(strs);
            convertSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertSuccess;
    }

    /**
     * 比较时间大小 source》target 若 target-source》=0则false，否则为true
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean compareDate(String source, String target) {
        boolean result = true;
        SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT_05);
        try {
            Date sourceDate = format.parse(source);
            Date targetDate = format.parse(target);
            if (sourceDate.compareTo(targetDate) > 0) {
                result = false;
            }
        } catch (ParseException ignored) {
            result = false;
        }
        return result;
    }

    /**
     * 比较时间大小 date与当前系统日期比较，若大于当前系统日期则false，否则为true
     *
     * @param date
     * @return
     */
    public static boolean compareDate(String date) {
        boolean result = true;

        LocalDate localDate = LocalDate.now();
        LocalDate sourceDate = LocalDate.parse(date);
        if (sourceDate.compareTo(localDate) > 0) {
            result = false;
        }

        return result;
    }

    /**
     * 比较时间大小 date与当前系统日期比较，若大于等于当前系统日期则false，否则为true
     *
     * @param date
     * @return
     */
    public static boolean compareDate2(String date) {
        boolean result = true;

        LocalDate localDate = LocalDate.now();
        LocalDate sourceDate = LocalDate.parse(date);
        if (sourceDate.compareTo(localDate) >= 0) {
            result = false;
        }

        return result;
    }

    /**
     * 判断是否为当前季度
     *
     * @param jd
     * @return
     */
    public static boolean isCurrentQuarter(String jd) {
        boolean result;
        LocalDate ldt = LocalDate.now();
        if (ldt.isBefore(ldt.with(Month.APRIL).withDayOfMonth(1))) {
            result = "一季度".equals(jd);
        } else if (ldt.isBefore(ldt.with(Month.JULY).withDayOfMonth(1))) {
            result = "二季度".equals(jd);
        } else if (ldt.isBefore(ldt.with(Month.OCTOBER).withDayOfMonth(1))) {
            result = "三季度".equals(jd);
        } else {
            result = "四季度".equals(jd);
        }
        return result;
    }

    /**
     * 获取当前季度
     *
     * @return
     */
    public static String getCurrentQuarter() {
        String result;
        LocalDate ldt = LocalDate.now();
        if (ldt.isBefore(ldt.with(Month.APRIL).withDayOfMonth(1))) {
            result = "1";
        } else if (ldt.isBefore(ldt.with(Month.JULY).withDayOfMonth(1))) {
            result = "2";
        } else if (ldt.isBefore(ldt.with(Month.OCTOBER).withDayOfMonth(1))) {
            result = "3";
        } else {
            result = "4";
        }
        return result;
    }

    /**
     * 获得当前年
     *
     * @return
     */
    public static String getYears() {
        LocalDate ldt = LocalDate.now();
        int year = ldt.getYear();
        return String.valueOf(year);
    }

    /**
     * 是否为当前月最后一天
     *
     * @return
     */
    public static boolean isLastDayOfMonth() {
        boolean result = false;
        int maxDayOfMonth = LocalDate.now().lengthOfMonth();
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        if (maxDayOfMonth == dayOfMonth) {
            result = true;
        }
        return result;
    }

    /**
     * 获取时间戳id
     *
     * @return
     */
    public static String getTimeStampId() {
        StringBuffer sb = new StringBuffer("");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddhh24mmssSSS");
        String timeStamp = LocalDateTime.now(ZoneOffset.of("+8")).format(dtf);
        sb.append(timeStamp);
        Random rn = new Random();
        int rnd = rn.nextInt(100000);
        String str = String.format("%06d", rnd);
        sb.append(str);
        return sb.toString();
    }

}
