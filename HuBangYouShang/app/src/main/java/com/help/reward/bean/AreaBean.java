package com.help.reward.bean;

/**
 * Created by admin on 2017/3/12.
 * 地址
 */

public class AreaBean {
    public String area_id;
    public String area_name;

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return area_name;
    }


}
