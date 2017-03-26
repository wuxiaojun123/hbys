package com.help.reward.network.api;


import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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

}
