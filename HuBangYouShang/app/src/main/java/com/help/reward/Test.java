package com.help.reward;

import com.help.reward.bean.OrderPulishedEvaluateBean;
import com.help.reward.utils.JsonUtils;
import com.idotools.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxiaojun on 2017/4/7.
 */

public class Test {

    public static void main(String[] args) throws ParseException {
        boolean isPost = false;
        if(!isPost){
            System.out.print("哈哈哈"+isPost);
        }

    }

    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;
    }

    public static String getStrTime(int addDay) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, addDay);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果

        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 例如：cc_time=1291778220
        re_StrTime = sdf.format(date);

        return re_StrTime;
    }

    public static List<String> getList() {
        List<String> list = new ArrayList<>(2);
        list.add("123.jpg");
        list.add("234.jpg");
        return list;
    }

    public static List<String> getList2() {
        List<String> list = new ArrayList<>(2);
        list.add("222222.jpg");
        list.add("333333.jpg");
        return list;
    }

}
