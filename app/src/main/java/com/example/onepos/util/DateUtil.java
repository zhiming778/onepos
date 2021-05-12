package com.example.onepos.util;

import android.app.Application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    private Application application;
    private static final String DATE_PATTERN = "MM-dd-yyyy";
    private static final String TIME_PATTERN = "hh:mm aa";


    public static long dateToMillis(String str) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        long millis = 0;
        try {
            Date date = df.parse(str);
            millis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }
    public static String millisToDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        DateFormat df = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        return df.format(calendar.getTime());
    }
    public static String millisToTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        DateFormat df = new SimpleDateFormat(TIME_PATTERN, Locale.US);
        return df.format(calendar.getTime());
    }
    public static long getStartMillis(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        return c.getTimeInMillis();
    }
    public static long getEndMillis(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        return (c.getTimeInMillis() + TimeUnit.DAYS.toMillis(1));
    }
}
