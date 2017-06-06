package com.help.reward.network.exception;

/**
 * Created by wuxiaojun on 17-6-6.
 */

public class ApiHttpResonseException extends RuntimeException {


    public ApiHttpResonseException(int resultCode,String msg){
        this.getApiHttpResonseMsg(resultCode,msg);
    }

    public ApiHttpResonseException(String detaileMsg){
        super(detaileMsg);
    }


    private static String getApiHttpResonseMsg(int code,String msg){
        if(code != 200){
            return msg;
        }
        return null;
    }


}
