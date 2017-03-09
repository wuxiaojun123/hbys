package com.help.reward.network.api;

import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.bean.Response.RegisterResponse;
import com.help.reward.bean.Response.VerificationCodeResponse;
import com.help.reward.utils.Constant;

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
    @POST(Constant.URL_LOGIN)
    Observable<LoginResponse> getLoginBean(
            @Field("username") String username,
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

    // 注册
    @FormUrlEncoded
    @POST(Constant.URL_REGISTER)
    Observable<RegisterResponse> getRegisterBean(
            @Field("phone") String phone,
            @Field("captcha") String captcha,
            @Field("password") String password,
            @Field("client") String client
    );

}
