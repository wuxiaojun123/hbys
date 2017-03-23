package com.help.reward.network.api;

import com.help.reward.bean.Response.AdInfoResponse;
import com.help.reward.bean.Response.AdvertisementResponse;
import com.help.reward.bean.Response.WatchAdGetScroeResponse;

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

    // 广告看完点赞 -- 详情页  ?act=advertisement&op=info
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<AdInfoResponse> getAdInfoResponse(
            @Query("act") String act,
            @Query("op") String curpage,
            @Field("id") String id
    );

    // 会员看广告  mobile/index.php?act=advertisement&op=watch
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<WatchAdGetScroeResponse> getWatchAdGetScroeResponse(
            @Query("act") String act,
            @Query("op") String curpage,
            @Field("key") String key,
            @Field("id") String id
    );

    // 搜索广告  mobile/index.php?act=advertisement&op=search
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<AdvertisementResponse> getSearchAdvertisementResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("keyword") String keyword
    );


}
