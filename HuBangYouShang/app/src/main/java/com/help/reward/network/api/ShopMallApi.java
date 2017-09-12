package com.help.reward.network.api;

import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.BrandResponse;
import com.help.reward.bean.Response.GoodEvaluateResponse;
import com.help.reward.bean.Response.GoodResponse;
import com.help.reward.bean.Response.GoodsSecondTypeResponse;
import com.help.reward.bean.Response.GoodsTypeResponse;
import com.help.reward.bean.Response.PayEndGoodsResponse;
import com.help.reward.bean.Response.SearchStoreResponse;
import com.help.reward.bean.Response.ShopMallMainResponse;
import com.help.reward.bean.Response.ShopSearchResponse;
import com.help.reward.bean.Response.StoreDetailAllResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
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
    @GET(Constant.URL_SHOP_MALL_MAIN)
    Observable<ShopMallMainResponse> getShopMallMainResponse();


    // 获取商城信息的数据 ?act=goods&op=goods_detail&goods_id=42
    @GET(Constant.URL_SHOP_MALL_INFO)
    Observable<GoodResponse> getGoodResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("goods_id") String goods_id,
            @Query("key") String key
    );

    /*// 获取商城信息的数据 ?act=goods&op=goods_detail&goods_id=42  post请求
    @GET(Constant.URL_SHOP_MALL_INFO)
    Observable<GoodResponse> getGoodResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("goods_id") String goods_id
    );*/


    @GET(Constant.URL_SHOP_MALL_INFO)
    Observable<BaseResponse> getGoodDetailsImgResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("goods_id") String goods_id
    );

    /**
     * @param goods_id 商品id
     * @param type     类型：1-好评，2-中评，3-差评，默认不填
     * @param curpage  分页，从1开始
     * @return
     *
     * ?act=goods&op=goods_evaluate
     */
    @FormUrlEncoded
    @POST(Constant.URL_SHOP_MALL_INFO_EVALUATE)
    Observable<GoodEvaluateResponse> getGoodDetailsEvaluateResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") int curpage,
            @Field("goods_id") String goods_id,
            @Field("type") String type
    );

    // 获取商城信息的数据 ?act=goods&op=goods_detail&goods_id=42
    @GET("mobile/index.php?act=index&op=search_key_list")
    Observable<ShopSearchResponse> getShopSearchResponse();


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

    /**
     * 全部商品-搜索
     * mobile/index.php?act=goods&op=goods_list
     * 参数[get]：
     * key不填或填错默认综合排序；
     * salenum 销量
     * clicknum 人气
     * price 价格
     * order desc降序asc升序；默认降序
     * price_from price_to价格区间
     * b_id 品牌id
     * keyword 关键字会计入cookie，显示在历史搜索里。
     * service：
     * freight 包邮
     * COD 货到付款
     * refund 急速退款
     * protection 消费者保障quality
     * 正品保障 sevenDay 7天无理由退货
     *
     * mobile/index.php?act=goods&op=goods_list
     */
    @FormUrlEncoded
    @POST(Constant.URL_SEARCHGOODS)
    Observable<StoreDetailAllResponse> getSearchGoodsResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") int curpage,
            @Field("key") String key,
            @Field("order") String order,
            @Field("price_from") String price_from,
            @Field("price_to") String price_to,
            @Field("b_id") String b_id,
            @Field("service[]") String[] service,
            @Field("keyword") String keyword,
            @Field("gc_id") String gc_id,
            @Field("store_id") String store_id
    );

    /**
     * 品牌列表
     * /mobile/index.php?act=goods_class&op=index
     * 参数[get]：gc_id 不填或填错取根类别，正确返回其子类别。
     * data: {class_list: [{
     */
    @GET(Constant.URL_BRANDLIST)
    Observable<BrandResponse> getBrandBeanResponse(

    );


    /**
     * 支付成功的商品列表
     * mobile/index.php?act=member_buy&op=pay_ok
     */
    @GET(Constant.URL_PAYOKGOODS)
    Observable<PayEndGoodsResponse> getPayEndGoodsResponse(
            @Query("key") String key
    );

    /**
     * 店铺搜索
     * mobile/index.php?act=store&op=listStore
     */
    @FormUrlEncoded
    @POST(Constant.URL_SEARCHSTORE)
    Observable<SearchStoreResponse> getSearchStoreResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") int curpage,
            @Field("keyword") String keyword
    );

}
