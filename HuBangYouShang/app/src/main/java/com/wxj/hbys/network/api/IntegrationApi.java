package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.AdvertisementResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public interface IntegrationApi {

    @FormUrlEncoded
    @POST("mobile/index.php?act=advertisement")
    Observable<AdvertisementResponse> getAdvertisementWatchPraise(
            @Field("type") String type
    );

}
