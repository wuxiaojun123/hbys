package com.help.reward.network;

import com.help.reward.App;
import com.help.reward.network.api.LoginApi;
import com.help.reward.network.api.PersonalApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 2017/3/1.
 */

public class PersonalNetwork {

    // 登录api
    public static LoginApi getLoginApi() {
        return RetrofitUtils.getSetCookieRetrofit().create(LoginApi.class);
    }

    // 获取个人的信息
    public static PersonalApi getResponseApi() {
        return RetrofitUtils.getRetrofitCookie().create(PersonalApi.class);
    }

}
