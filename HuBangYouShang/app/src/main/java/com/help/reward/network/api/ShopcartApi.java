package com.help.reward.network.api;

import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ADBrian on 26/03/2017.
 */

public interface ShopcartApi {

    @FormUrlEncoded
    @POST(Constant.URL_SHOPCART_QUERY)
    Observable<BaseResponse> getShopcartList(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_SHOPCART_ADD)
    Observable<BaseResponse> getShopcartAdd(
            @Field("key") String key,
            @Field("goods_id") String goods_id,
            @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST(Constant.URL_SHOPCART_DELETE)
    Observable<BaseResponse> getShopcartDelete(
            @Field("key") String key,
            @Field("cart_id") String cart_id
    );

}
