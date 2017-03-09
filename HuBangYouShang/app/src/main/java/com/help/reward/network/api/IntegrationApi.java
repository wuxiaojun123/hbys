package com.help.reward.network.api;

import com.help.reward.bean.Response.AdvertisementResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public interface IntegrationApi {

    // 广告看完点赞
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<AdvertisementResponse> getAdvertisementWatchPraise(
            @Query("act") String act,
            @Query("curpage") String curpage,
            @Field("type") String type
    );

    // 广告加群购买
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<AdvertisementResponse> getAdvertisementGroupBuying(
            @Query("act") String act,
            @Query("curpage") String curpage,
            @Field("type") String type
    );



}
