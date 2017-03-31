package com.help.reward.network.api;

import com.help.reward.bean.Response.GoodResponse;
import com.help.reward.bean.Response.GoodsSecondTypeResponse;
import com.help.reward.bean.Response.GoodsTypeResponse;
import com.help.reward.bean.Response.ShopMallMainResponse;
import com.help.reward.bean.Response.ShopSearchResponse;
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

    // 获取商城信息的数据 ?act=goods&op=goods_detail&goods_id=42
    @GET("mobile/index.php?act=index&op=search_key_list")
    Observable<ShopSearchResponse> getShopSearchResponse(
    );



    /**
     * 类别列表
     * /mobile/index.php?act=goods_class&op=index
     * 参数[get]：gc_id 不填或填错取根类别，正确返回其子类别。
     * data: {class_list: [{
     */
    @GET(Constant.URL_GOODSCLASS)
    Observable<GoodsTypeResponse> getGoodClassResponse(
    );

    /**
     * 二级类别列表
     * /mobile/index.php?act=goods_class&op=index
     * 参数[get]：gc_id 不填或填错取根类别，正确返回其子类别。
     * data: {class_list: [{
     */
    @GET(Constant.URL_GOODSCLASS)
    Observable<GoodsSecondTypeResponse> getGoodSecondClassResponse(
            @Query("gc_id") String gc_id
    );
}
