package com.help.reward.network.api;

import com.help.reward.bean.Response.AddSellerGroupResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.CommitOrderResponse;
import com.help.reward.bean.Response.ConfirmOrderResponse;
import com.help.reward.bean.Response.PayTypeAlipayResponse;
import com.help.reward.bean.Response.PayTypeResponse;
import com.help.reward.bean.Response.PayTypeWchatResponse;
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

    // 商家群
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_group&op=join_group")
    Observable<AddSellerGroupResponse> getAddSellerGroup(
            @Field("key") String key,
            @Field("member_id") String member_id
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
     * @param key
     * @param cart_id
     * @param ifcart     0 ，直接购买，1，购物车
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


    /***
     * cart_id;ifcart;address_id;vat_hash;offpay_hash;offpay_hash_batch;pay_name(字符串’online’)，
     * voucher 类似‘t_id|store_id|price,t_id|store_id|price,...’格式
     * general_voucher类似‘store_id|num,store_id|num,...’格式
     * pay_message 格式 店铺id|备注内容,店铺id|备注内容
     * @param key
     * @param cart_id
     * @param ifcart
     * @param address_id
     * @param vat_hash
     * @param offpay_hash
     * @param offpay_hash_batch
     * @param pay_name
     * @return
     */
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
            @Field("pay_name") String pay_name,
            @Field("voucher") String voucher,
            @Field("general_voucher") String general_voucher,
            @Field("pay_message") String pay_message
    );

    // 选择支付方式
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_payment&op=pay")
    Observable<PayTypeResponse> getPayTypeResponse(
            @Field("pay_sn") String pay_sn,
            @Field("key") String key
    );


    // 微信支付
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_payment&op=wx_app_pay3")
    Observable<PayTypeWchatResponse> getPayTypeWchatResponse(
            @Field("pay_sn") String pay_sn,
            @Field("key") String key
    );

    // 支付宝支付
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_payment&op=alipay_pay")
    Observable<PayTypeAlipayResponse> getPayTypeAliPayResponse(
            @Field("pay_sn") String pay_sn,
            @Field("key") String key
    );

}
