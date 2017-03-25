package com.reward.help.merchant.network.api;


import com.reward.help.merchant.bean.Response.LoginResponse;
import com.reward.help.merchant.bean.Response.VerificationCodeResponse;
import com.reward.help.merchant.utils.Constant;

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

    // 登录
    @FormUrlEncoded
    @POST(Constant.URL_SELLER_LOGIN)
    Observable<LoginResponse> getLoginBean(
            @Field("phone") String username,
            @Field("password") String password,
            @Field("client") String client
    );

    // 获取code 注册和修改密码
    @FormUrlEncoded
    @POST(Constant.URL_GET_CODE)
    Observable<VerificationCodeResponse> getVerificationCodeBean(
            @Field("phone") String phone,
            @Field("type") String type
    );

}
