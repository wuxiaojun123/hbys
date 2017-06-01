package com.help.reward.network.api;

import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.CommitOrderResponse;
import com.help.reward.bean.Response.ConfirmOrderResponse;
import com.help.reward.bean.Response.ShopCartResponse;
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
    Observable<ShopCartResponse> getShopcartList(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_SHOPCART_ADD)
    Observable<BaseResponse> getShopcartAdd(
            @Field("key") String key,
            @Field("goods_id") String goods_id,
            @Field("quantity") String quantity
    );

    // 收藏商品
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_favorites&op=favorites_add")
    Observable<BaseResponse> getCollectionShopss(
            @Field("key") String key,
            @Field("goods_id") String goods_id
    );



    @FormUrlEncoded
    @POST(Constant.URL_SHOPCART_DELETE)
    Observable<BaseResponse> getShopcartDelete(
                    @Field("key") String key,
                    @Field("cart_id") String cart_id
            );

    @FormUrlEncoded
    @POST(Constant.URL_SHOPCART_EDIT)
    Observable<BaseResponse> getShopcartEdit(
            @Field("key") String key,
            @Field("cart_id") String cart_id,
            @Field("quantity") int quantity
    );

    /**
     *
     * @param key
     * @param cart_id
     * @param ifcart 0 ，直接购买，1，购物车
     * @param address_id
     * @return
     */
    @FormUrlEncoded
    @POST(Constant.URL_BUY_STEP_ONE)
    Observable<ConfirmOrderResponse> getComfirmOrderList(
            @Field("key") String key,
            @Field("cart_id") String cart_id,
            @Field("ifcart") String ifcart,
            @Field("address_id") String address_id
            );

    @FormUrlEncoded
    @POST(Constant.URL_BUY_STEP_TWO)
    Observable<CommitOrderResponse> commitComfirmOrderList(
            @Field("key") String key,
            @Field("cart_id") String cart_id,
            @Field("ifcart") String ifcart,
            @Field("address_id") String address_id,
            @Field("vat_hash") String vat_hash,
            @Field("offpay_hash") String offpay_hash,
            @Field("offpay_hash_batch") String offpay_hash_batch,
            @Field("pay_name") String pay_name
            );

}
