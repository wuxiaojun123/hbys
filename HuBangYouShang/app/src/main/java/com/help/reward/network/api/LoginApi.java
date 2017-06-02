package com.help.reward.network.api;

import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.bean.Response.LoginResponse2;
import com.help.reward.bean.Response.RegisterResponse;
import com.help.reward.bean.Response.VerificationCodeResponse;
import com.help.reward.bean.Response.WXLoginTokenResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    // 退出登录
    @FormUrlEncoded
    @POST("/mobile/index.php?act=logout")
    Observable<BaseResponse<String>> getLogoutResponse(
            @Field("client") String client,
            @Field("key") String key
    );

    /*// 微信-登录-获取access_token  ?act=connect_wx&op=index
    @GET("/mobile/index.php")
    Observable<WXLoginTokenResponse> getWXLoginTokenResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("code") String code
    );

    // 微信-登录-获取access_token ?act=connect_wx&op=login
    @GET("/mobile/index.php")
    Observable<LoginResponse> getWXLoginResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("access_token") String access_token,
            @Query("openid") String openid,
            @Query("client") String client
    );*/

    // 微信-登录-获取access_token  ?act=connect_wx&op=index
    @FormUrlEncoded
    @POST("/mobile/index.php?act=connect_wx&op=index")
    Observable<WXLoginTokenResponse> getWXLoginTokenResponse(
//            @Field("act") String act,
//            @Field("op") String op,
            @Field("code") String code
    );

    // 微信-登录-获取access_token ?act=connect_wx&op=login
    @FormUrlEncoded
    @POST("/mobile/index.php?act=connect_wx&op=login")
    Observable<LoginResponse2> getWXLoginResponse(
//            @Field("act") String act,
//            @Field("op") String op,
            @Field("access_token") String access_token,
            @Field("openid") String openid,
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

    // 重设密码
    @FormUrlEncoded
    @POST("mobile/index.php?act=connect&op=find_password_ww")
    Observable<RegisterResponse> getFindPasswordBean(
            @Field("phone") String phone,
            @Field("captcha") String captcha,
            @Field("password") String password,
            @Field("client") String client
    );


    // 找回密码---验证码验证
    @FormUrlEncoded
    @POST("/mobile/index.php?act=connect&op=check_captcha")
    Observable<BaseResponse<String>> getCheckCaptchaBean(
            @Field("phone") String phone,
            @Field("auth_code") String authcode
    );

    // 找回密码---重设密码
    @FormUrlEncoded
    @POST("/mobile/index.php?act=connect&op=reset_password")
    Observable<BaseResponse<String>> getResetPasswordBean(
            @Field("password") String password,
            @Field("password1") String password1,
            @Field("phone") String phone
    );


}
