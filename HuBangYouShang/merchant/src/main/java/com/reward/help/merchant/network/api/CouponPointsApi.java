package com.reward.help.merchant.network.api;

import com.reward.help.merchant.bean.Response.CouponListResponse;
import com.reward.help.merchant.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 优惠券和积分
 */

public interface CouponPointsApi {

    // 登录
    @FormUrlEncoded
    @POST(Constant.URL_LOGIN)
    Observable<CouponListResponse> getCouponList(
            @Field("key") String key
    );
}
