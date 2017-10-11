package com.help.reward.bean;

/**
 * Created by wuxiaojun on 17-6-6.
 */

public class AddDayBean {

    public String day; // x天
    public String time; // yyyy/MM/dd HH/mm/ss

    public AddDayBean(String day,String time){
        this.day = day;
        this.time = time;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return day;
    }

}
