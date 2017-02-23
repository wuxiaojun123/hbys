package com.wxj.hbys.bean.Response;

/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class LoginResponse extends BaseResponse{

    public LoginBean data;

    public class LoginBean{
        public String username;
        public String userid;
        public String key;
    }

}
