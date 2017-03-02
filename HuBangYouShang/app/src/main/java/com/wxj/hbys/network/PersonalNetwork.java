package com.wxj.hbys.network;

import com.wxj.hbys.network.api.IntegrationApi;
import com.wxj.hbys.network.api.LoginApi;
import com.wxj.hbys.network.api.PersonalApi;
import com.wxj.hbys.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 2017/3/1.
 */

public class PersonalNetwork {

    // 登录api
    public static LoginApi getLoginApi() {
        return RetrofitUtils.getRetrofit().create(LoginApi.class);
    }

    // 获取个人的信息
    public static PersonalApi getMyHelpPostResponseApi(){
        return RetrofitUtils.getRetrofitCookie().create(PersonalApi.class);
    }

}
