package com.help.reward.bean;

/**
 * Created by wuxiaojun on 17-3-24.
 */

public class SexBean {

    public String sex_id;
    public String sex_name;

    public SexBean(String sex_id,String sex_name){
        this.sex_id = sex_id;
        this.sex_name = sex_name;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return sex_name;
    }

}   
