package com.reward.help.merchant.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    final static String patten = "yyyy.MM.dd";
    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        Date strtodate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(patten);
            ParsePosition pos = new ParsePosition(0);
            strtodate = formatter.parse(strDate, pos);
        }catch(Exception e){
        }
        return strtodate;
    }

    /**  * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss  *   * @param dateDate  * @return  */
    public static String dateToStrLong(Date dateDate) {
        String dateString = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(patten);
            dateString = formatter.format(dateDate);
        } catch (Exception e){

        }
        return dateString;
    }

    public static String strDateToStr(String dateDate) {
        String dateString = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(patten);
            dateString = formatter.format(new Date(Long.parseLong(dateDate)));
        } catch (Exception e){

        }
        return dateString;
    }
}
