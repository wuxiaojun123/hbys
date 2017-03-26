package com.help.reward.bean;

/**
 * Created by admin on 2017/3/12.
 * 分类
 */

public class HelpBoardBean {
    public String board_id;
    public String board_name;

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return board_name;
    }


}
