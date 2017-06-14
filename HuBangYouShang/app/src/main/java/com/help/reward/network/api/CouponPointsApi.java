package com.help.reward.network.api;


import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.CouponsRecordResponse;
import com.help.reward.bean.Response.PointsRecordResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface CouponPointsApi {

    @FormUrlEncoded
    @POST(Constant.URL_RECEIVE_COUPON)
    Observable<BaseResponse> receiveCoupon(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    @FormUrlEncoded
    @POST(Constant.URL_RECEIVE_POINTS)
    Observable<BaseResponse> receivePoints(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    // 我的获赏----跟帖  ?act=member_points&op=receivePointsLog
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<PointsRecordResponse> receivePointsLog(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key
    );

    // 我的获赏----发帖  mobile/index.php?act=member_voucher&op=receiveVoucherLog
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<CouponsRecordResponse> receiveCouponsLog(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key
    );

}
