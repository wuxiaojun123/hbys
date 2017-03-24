package com.reward.help.merchant.network;

import com.reward.help.merchant.network.api.LoginApi;
import com.reward.help.merchant.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 2017/3/1.
 */

public class PersonalNetwork {

    // 登录api
    public static LoginApi getLoginApi() {
        return RetrofitUtils.getRetrofit().create(LoginApi.class);
    }

//    // 获取个人的信息
//    public static PersonalApi getResponseApi(){
//        return RetrofitUtils.getRetrofitCookie().create(PersonalApi.class);
//    }

}
