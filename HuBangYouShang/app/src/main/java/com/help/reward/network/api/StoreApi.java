package com.help.reward.network.api;

import com.help.reward.bean.Response.StoreDetailAllResponse;
import com.help.reward.bean.Response.StoreDetailHomeResponse;
import com.help.reward.bean.Response.StoreDetailNewResponse;
import com.help.reward.bean.Response.StoreKindsResponse;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public interface StoreApi {

    /**
     * 店铺首页
     * /mobile/index.php?act=store&op=store_info
     * 参数[post]：store_id 2
     * 返回：{store_info:{},rec_goods_list:[],sale_goods_list:[]}
     */
    @FormUrlEncoded
    @POST(Constant.URL_STORE)
    Observable<StoreDetailHomeResponse> getStoreDetailHomeResponse(
            @Field("key") String key,
            @Query("op") String op,
            @Field("store_id") String store_id
    );


    /**
     * 店铺商品分类
     * mobile/index.php?act=store&op=store_goods_class
     * 参数[post]：store_id 2
     * {store_goods_class:[]}
     */
    @FormUrlEncoded
    @POST(Constant.URL_STORE)
    Observable<StoreKindsResponse> getStoreGoodsClassResponse(
            @Query("op") String op,
            @Field("store_id") String store_id
    );


    /**
     * 店铺全部商品-搜索
     * /mobile/index.php?act=store&op=store_goods
     * 参数[post]：
     * key不填或填错默认综合排序；salenum 销量 price 价格
     * order desc降序asc升序；默认降序
     * price_from price_to价格区间
     * stc_id店铺分类id
     * keyword 关键字商品名称。
     */
    @FormUrlEncoded
    @POST(Constant.URL_STORE)
    Observable<StoreDetailAllResponse> getStoreGoodsResponse(
            @Query("op") String op,
            @Field("key") String key,
            @Field("store_id") String store_id,
            @Field("order") String order,
            @Field("stc_id") String stc_id,
            @Field("keyword") String keyword,
            @Query("curpage") int curpage
    );

    /**
     * 新品
     * /mobile/index.php?act=store&op=store_new_goods
     * 参数post：store_id
     */
    @FormUrlEncoded
    @POST(Constant.URL_STORE)
    Observable<StoreDetailNewResponse> getStoreDetailNewResponse(
            @Field("key") String key,
            @Query("op") String op,
            @Field("store_id") String store_id,
            @Field("curpage") int curpage
    );

    /**
     * 收藏店铺
     * /mobile/index.php?act=member_favorites_store&op=favorites_add
     * 参数post：store_id
     */
    @FormUrlEncoded
    @POST(Constant.URL_STORE_ADD)
    Observable<StringResponse> getAddFavoritesStoreResponse(
            @Field("key") String key,
            @Field("store_id") String store_id
    );

}
