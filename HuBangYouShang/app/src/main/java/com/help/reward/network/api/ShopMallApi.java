package com.help.reward.network.api;

import com.help.reward.bean.Response.GoodResponse;
import com.help.reward.bean.Response.ShopMallMainResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public interface ShopMallApi {

    // 获取商城首页的数据
    @GET(Constant.URL_SHOP_MALL_MAIN)
    Observable<ShopMallMainResponse> getShopMallMainResponse();


    // 获取商城信息的数据 ?act=goods&op=goods_detail&goods_id=42
    @GET(Constant.URL_SHOP_MALL_INFO)
    Observable<GoodResponse> getGoodResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("goods_id") String goods_id
    );

}
