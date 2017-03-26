package com.help.reward.bean;

/**
 * Created by wuxiaojun on 2017/3/26.
 */

public class BusinessBean {

    public String business_id = "0";
    public String business_name;

    public BusinessBean(String business_name){
        this.business_name = business_name;
    }


    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return business_name;
    }


}
