package com.elite.tetto.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    
    
    public static Date getNowDate() {
        return new Date();
    }
    
    public static Date getYesterdayDate() {
        Date date = new Date();
        date.setTime(date.getTime() - 24 * 60 * 60 * 1000);
        return date;
    }
    
    public static String getNowDateStr() {
        return format(getNowDate());
    }
    
    public static String getYesterdayDateStr() {
        return format(getYesterdayDate());
    }
    
    public static String format(Date date) {
        return DateUtil.format(date, "yyyy-MM-dd");
    }
    
    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
    
    
}
