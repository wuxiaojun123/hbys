package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.GoodResponse;
import com.wxj.hbys.bean.Response.ShopMallMainResponse;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public interface ShopMallApi {

    // 获取商城首页的数据
    @GET("mobile/index.php?act=shop&op=index")
    Observable<ShopMallMainResponse> getShopMallMainResponse();

    // 获取商城信息的数据 ?act=goods&op=goods_detail&goods_id=42
    @GET("mobile/index.php")
    Observable<GoodResponse> getGoodResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("goods_id") String goods_id
    );

}
