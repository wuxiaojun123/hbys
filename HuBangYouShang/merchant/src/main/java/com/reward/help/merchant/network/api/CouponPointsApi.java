package com.reward.help.merchant.network.api;

import com.reward.help.merchant.bean.Response.BaseResponse;
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
    @POST(Constant.URL_GET_COUPON_LIST)
    Observable<CouponListResponse> getCouponList(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_SEND_COUPON)
    Observable<BaseResponse> sendCoupon(
            @Field("key") String key,
            @Field("voucher_t_id") String voucher_t_id,
            @Field("num") String num
    );
}