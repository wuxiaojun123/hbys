package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.LoginResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 *
 * http://210.72.13.135/mobile/index.php?act=login
 * Created by wuxiaojun on 2017/2/22.
 */

public interface LoginApi {

    @FormUrlEncoded
    @POST("mobile/index.php?act=login")
    Observable<LoginResponse> getLoginBean(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client") String client
    );

}
