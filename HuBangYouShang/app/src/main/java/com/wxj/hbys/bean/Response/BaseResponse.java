package com.wxj.hbys.bean.Response;


/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class BaseResponse<T> {

    public int code; // 200 正常  201异常
    public String msg;// 信息
    public T data;


    @Override
    public String toString() {
        return code + "msg=" + msg + data + "=====";
    }

}
