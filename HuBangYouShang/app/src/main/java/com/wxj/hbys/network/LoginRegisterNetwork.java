package com.wxj.hbys.network;

import com.wxj.hbys.network.api.LoginApi;
import com.wxj.hbys.utils.Constant;

import retrofit2.Retrofit;

/**
 * zhouying 123456
 * <p>
 * Created by wuxiaojun on 2017/2/22.
 */

public class LoginRegisterNetwork {

    private static LoginApi mLoginApi;

    public static LoginApi getLoginApi() {
        if (mLoginApi == null) {
            mLoginApi = RetrofitUtils.getRetrofit().create(LoginApi.class);
        }
        return mLoginApi;
    }


}
