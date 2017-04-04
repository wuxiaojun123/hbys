package com.reward.help.merchant.network.api;

import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.CouponListResponse;
import com.reward.help.merchant.bean.Response.QueryCouponResponse;
import com.reward.help.merchant.bean.Response.QueryMyPointsResponse;
import com.reward.help.merchant.bean.Response.QueryPointsResponse;
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

    @FormUrlEncoded
    @POST(Constant.URL_QUERY_COUPON)
    Observable<QueryCouponResponse> queryCoupon(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    @FormUrlEncoded
    @POST(Constant.URL_SEND_POINTS)
    Observable<BaseResponse> sendPoints(
            @Field("key") String key,
            @Field("people_limit") String people_limit,
            @Field("num_limit") String num_limit
    );

    @FormUrlEncoded
    @POST(Constant.URL_QUERY_POINTS)
    Observable<QueryPointsResponse> queryPoints(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    @FormUrlEncoded
    @POST(Constant.URL_GET_POINTS)
    Observable<QueryMyPointsResponse> getPoints(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_GROUP_APPLY)
    Observable<BaseResponse> apply(
            @Field("key") String key,
            @Field("content") String content
    );
}