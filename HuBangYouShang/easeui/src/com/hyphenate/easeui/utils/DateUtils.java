package com.hyphenate.easeui.utils;

import android.annotation.SuppressLint;

import com.hyphenate.util.TimeInfo;

import android.annotation.SuppressLint;
import com.hyphenate.util.TimeInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * Created by wuxiaojun on 17-8-9.
 */

public class DateUtils extends com.hyphenate.util.DateUtils{

    public static String getTimestampString(Date var0) {
        String var1 = null;
        String var2 = Locale.getDefault().getLanguage();
        boolean var3 = var2.startsWith("zh");
        long var4 = var0.getTime();
        if(isSameDay(var4)) {
            if(var3) {
                var1 = "aa HH:mm";
            } else {
                var1 = "HH:mm aa";
            }
        } else if(isYesterday(var4)) {
            if(!var3) {
                return "Yesterday " + (new SimpleDateFormat("HH:mm aa", Locale.ENGLISH)).format(var0);
            }

            var1 = "昨天aa HH:mm";
        } else if(var3) {
            var1 = "M月d日aa HH:mm";
        } else {
            var1 = "MMM dd HH:mm aa";
        }

        return var3?(new SimpleDateFormat(var1, Locale.CHINESE)).format(var0):(new SimpleDateFormat(var1, Locale.ENGLISH)).format(var0);
    }

    private static boolean isSameDay(long var0) {
        TimeInfo var2 = getTodayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    private static boolean isYesterday(long var0) {
        TimeInfo var2 = getYesterdayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

}
