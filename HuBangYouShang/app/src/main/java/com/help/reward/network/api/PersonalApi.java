package com.help.reward.network.api;

import com.help.reward.bean.CertificationResponse;
import com.help.reward.bean.Response.AddAddressResponse;
import com.help.reward.bean.Response.AddressResponse;
import com.help.reward.bean.Response.AdvertisementResponse;
import com.help.reward.bean.Response.AeraResponse;
import com.help.reward.bean.Response.BalanceExchangeResponse;
import com.help.reward.bean.Response.BalanceExchangeVolumeResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.BusinessResponse;
import com.help.reward.bean.Response.CouponDetailsResponse;
import com.help.reward.bean.Response.CouponTradingResponse;
import com.help.reward.bean.Response.DiscountAmountResponse;
import com.help.reward.bean.Response.GeneralExchangeVolumeResponse;
import com.help.reward.bean.Response.GeneralVolumeResponse;
import com.help.reward.bean.Response.HelpCenterResponse;
import com.help.reward.bean.Response.HelpRewardResponse;
import com.help.reward.bean.Response.MemberInfoResponse;
import com.help.reward.bean.Response.MyBalanceResponse;
import com.help.reward.bean.Response.MyCollectionGoodsResponse;
import com.help.reward.bean.Response.MyCollectionPostResponse;
import com.help.reward.bean.Response.MyCollectionStoreResponse;
import com.help.reward.bean.Response.MyCouponResponse;
import com.help.reward.bean.Response.MyHelpCommentResponse;
import com.help.reward.bean.Response.MyHelpPostResponse;
import com.help.reward.bean.Response.MyOrderResponse;
import com.help.reward.bean.Response.MyRewardCommentResponse;
import com.help.reward.bean.Response.MyRewardPostResponse;
import com.help.reward.bean.Response.MyVoteResponse;
import com.help.reward.bean.Response.OrderInfoResponse;
import com.help.reward.bean.Response.PersonInfoResponse;
import com.help.reward.bean.Response.SaveNewAddressResponse;
import com.help.reward.bean.Response.UploadHeadImageReponse;
import com.help.reward.utils.Constant;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=member_index&op=my_seek_help
 * <p>
 * Created by wuxiaojun on 17-3-1.
 */

public interface PersonalApi {

    // mobile/index.php?act=logout 退出登录 post传client

    // 个人信息--获取地区 ?act=area
    @GET("mobile/index.php")
    Observable<AeraResponse> getAeraResponse(
            @Query("act") String act,
            @Query("area_id") String area_id
    );

    // 个人信息---行业
    @FormUrlEncoded
    @POST("mobile/index.php?act=index&op=get_config")
    Observable<BusinessResponse> getBusinessResponse(
            @Field("config_name") String config_name
    );

    // 我的个人信息
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=member_info")
    Observable<PersonInfoResponse> getPersonInfoResponse(
            @Field("key") String key
    );

    // 我的个人信息--提交sex 0/1/2；business;position;description;avatar(上传后的url);province;city;area(省市区id);name
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=edit_member_info")
    Observable<BaseResponse<String>> getUpdatePersonInfoResponse(
            @Field("key") String key,
            @Field("sex") String sex,
            @Field("business") String business,
            @Field("position") String position,
            @Field("description") String description,
            @Field("avatar") String avatar,
            @Field("province") String province,
            @Field("city") String city,
            @Field("area") String area,
            @Field("name") String name
    );

    // 上传头像
    @Multipart
    @POST("mobile/index.php?act=upload_file&op=upload_img")
    Observable<UploadHeadImageReponse> getUploadHeadImageReponse(
            // @Part("description") RequestBody description,
            @Part RequestBody file,
            @Query("type") String type,
            @Query("key") String key
    );

    /**
     * 上传图片
     *
     * @return
     */
    @Multipart
    @POST("mobile/index.php?act=upload_file&op=upload_img")
    Observable<UploadHeadImageReponse> uploadImage(
            @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part part);

    // 认证--获取状态
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=certification")
    Observable<CertificationResponse> getCertificationStateResponse(
            @Field("key") String key
    );

    // 认证--提交
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=certification_post")
    Observable<BaseResponse<String>> getCertificationPostResponse(
            @Field("member_truename") String member_truename,
            @Field("ID_card") String ID_card,
            @Field("identity_img") String identity_img,
            @Field("key") String key
    );

    // 手机认证---验证码验证
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_account&op=modify_mobile_step3")
    Observable<BaseResponse<String>> getModifyMobileStep3(
            @Field("key") String key,
            @Field("phone") String phone,
            @Field("auth_code") String authcode
    );

    // 手机认证---重设密码
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_account&op=modify_mobile_step5")
    Observable<BaseResponse<String>> getModifyMobileStep5(
            @Field("key") String key,
            @Field("password") String password
    );

    // 登录密码---验证码验证
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_account&op=modify_password_step3")
    Observable<BaseResponse<String>> getModifyPwdStep3(
            @Field("key") String key,
            @Field("phone") String phone,
            @Field("auth_code") String authcode
    );

    // 登录密码---重设密码
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_account&op=modify_password_step5")
    Observable<BaseResponse<String>> getModifyPwdStep5(
            @Field("key") String key,
            @Field("password") String password,
            @Field("password1") String password1
    );

    // 支付密码---验证码验证
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_account&op=modify_paypwd_step3")
    Observable<BaseResponse<String>> getModifyPayStep3(
            @Field("key") String key,
            @Field("phone") String phone,
            @Field("auth_code") String authcode
    );

    // 支付密码---重设密码
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_account&op=modify_password_step5")
    Observable<BaseResponse<String>> getModifyPayStep5(
            @Field("key") String key,
            @Field("password") String password,
            @Field("password1") String password1
    );


    // 地址管理
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_address&op=address_list")
    Observable<AddressResponse> getAddressResponse(
            @Field("key") String key
    );

    // 删除地址
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_address&op=address_del")
    Observable<BaseResponse<String>> getRemoveAddressResponse(
            @Field("key") String key,
            @Field("address_id") String address_id
    );

    // 新增地址
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_address&op=address_add")
    Observable<SaveNewAddressResponse> getAddAddressResponse(
            @Field("key") String key,
            @Field("true_name") String true_name,
            @Field("mob_phone") String mob_phone,
            @Field("pro_id") String pro_id,
            @Field("area_id") String area_id,
            @Field("city_id") String cite_id,
            @Field("area_info") String area_info,
            @Field("address") String address
    );

    // 编辑地址
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_address&op=address_edit")
    Observable<BaseResponse<String>> getEditAddressResponse(
            @Field("key") String key,
            @Field("address_id") String address_id,
            @Field("true_name") String true_name,
            @Field("mob_phone") String mob_phone,
            @Field("pro_id") String pro_id,
            @Field("area_id") String area_id,
            @Field("city_id") String cite_id,
            @Field("area_info") String area_info,
            @Field("address") String address
    );

    // 设为默认
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_address&op=set_default")
    Observable<AddressResponse> getSetDefaultAddressResponse(
            @Field("key") String key,
            @Field("address_id") String address_id
    );

    // 我的求助--发帖
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=my_seek_help")
    Observable<MyHelpPostResponse> getMyHelpPostResponse(
            @Field("type") String type,
            @Field("key") String key
    );

    // 我的求助--跟帖
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=my_seek_help")
    Observable<MyHelpCommentResponse> getMyHelpCommentResponse(
            @Field("type") String type,
            @Field("key") String key
    );

    // 我的获赏--发帖
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=my_get_reward")
    Observable<MyRewardPostResponse> getMyRewardPostResponse(
            @Field("type") String type,
            @Field("key") String key
    );

    // 我的获赏--跟帖
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=my_get_reward")
    Observable<MyRewardCommentResponse> getMyRewardCommentResponse(
            @Field("type") String type,
            @Field("key") String key
    );

    // 我的投票
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_vote&op=list")
    Observable<MyVoteResponse> getMyVoteResponse(
            @Field("key") String key
    );

    // 我的收藏--帖子
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_favorites_post&op=favorites_list")
    Observable<MyCollectionPostResponse> getMyCollectionPostResponse(
            @Field("key") String key
    );

    // 我的收藏--删除帖子
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_favorites_post&op=favorites_del")
    Observable<BaseResponse> getDeleteMyCollectionPostResponse(
            @Field("key") String key,
            @Field("post_id") String post_id,
            @Field("type") String type
    );

    // 我的收藏--删除商品
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_favorites&op=favorites_del")
    Observable<BaseResponse> getDeleteMyCollectionGoodsResponse(
            @Field("key") String key,
            @Field("fav_id") String post_id
    );

    // 我的收藏--删除店铺
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_favorites_store&op=favorites_del")
    Observable<BaseResponse> getDeleteMyCollectionStoreResponse(
            @Field("key") String key,
            @Field("store_id") String post_id
    );

    // 我的收藏--商品
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_favorites&op=favorites_list")
    Observable<MyCollectionGoodsResponse> getMyCollectionGoodsResponse(
            @Field("key") String key
    );

    // 我的收藏--店铺
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_favorites_store&op=favorites_list")
    Observable<MyCollectionStoreResponse> getMyCollectionStoreResponse(
            @Field("key") String key
    );

    // 我的账户--余额明细  ?act=member_fund&op=predepositlog
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_BALANCE)
    Observable<MyBalanceResponse> getMyBalanceResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("type") String type
    );

    // 我的账户--帮赏分明细
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_ACCOUNT_HELP_REWARD)
    Observable<HelpRewardResponse> getMyHelpRewardResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("type") String type
    );

    // 我的账户--通用卷明细  ?act=member_general_voucher&op=detail_lo
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_ACCOUNT_GENERAL_VOLUME)
    Observable<GeneralVolumeResponse> getMyGeneralVolumeResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("type") String type
    );

    // 我的账户--我的优惠卷  mobile/index.php?act=member_voucher&op=voucher_list
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_ACCOUNT_MY_COUPON)
    Observable<MyCouponResponse> getMyCouponResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("voucher_state") String voucher_state
    );

    // 我的账户--优惠金额  mobile/index.php?act=member_voucher&op=voucher_list
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=discount_level")
    Observable<DiscountAmountResponse> getDiscountAmountResponse(
            @Field("key") String key
    );


    // 我的账户--优惠劵交易  mobile/index.php?act=voucher&op=voucher_list
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<CouponTradingResponse> getCouponTradingResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("storename") String storename,
            @Field("goodsname") String goodsname,
            @Field("order") String order
    );

    // 我的账户--优惠劵交易，查询优惠劵交易的结果展示
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<CouponTradingResponse> getCouponTradingResultResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("storename") String storename,
            @Field("order") String order,
            @Field("goodsname") String goodsname
    );

    // 我的账户--优惠劵详情
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_voucher&op=voucher_info")
    Observable<CouponDetailsResponse> getCouponDetailsResponse(
            @Field("key") String key,
            @Field("voucher_id") String voucher_id
    );

    // 我的账户--优惠劵详情--我要交易
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_voucher&op=buy")
    Observable<BaseResponse<String>> getCouponPutOnSaleResponse(
            @Field("key") String key,
            @Field("voucher_id") String voucher_id,
            @Field("price") int price
    );

    // 我的账户--优惠劵详情--优惠劵下架
    @FormUrlEncoded
    @POST("/mobile/index.php?act=member_voucher&op=withdraw")
    Observable<BaseResponse<String>> getCouponWithdrawResponse(
            @Field("key") String key,
            @Field("voucher_id") String voucher_id
    );

    // 我的账户--优惠劵详情--确认交易
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_voucher&op=put_on_sale")
    Observable<BaseResponse<String>> getCouponBuyResponse(
            @Field("key") String key,
            @Field("voucher_id") String voucher_id,
            @Field("price") int price
    );

    // 余额兑换帮赏分
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_YUE_DUIHUAN_BANGSHANGFEN)
    Observable<BalanceExchangeResponse> getBalanceExchangeResponse(
            @Field("key") String key
    );

    // 余额兑换帮赏分--提交
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_COMMIT_YUE_DUIHUAN_BANGSHANGFEN)
    Observable<BaseResponse<String>> getBalanceExchangeStringResponse(
            @Field("key") String key,
            @Field("exchange") String exchange
    );

    // 帮赏分兑换通用卷
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_BANGSHANGFEN_DUIHUAN_TONGYONGJUAN)
    Observable<GeneralExchangeVolumeResponse> getGeneralExchangeVolumeResponse(
            @Field("key") String key
    );

    // 帮赏分兑换通用卷--提交
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_COMMIT_BANGSHANGFEN_DUIHUAN_TONGYONGJUAN)
    Observable<BaseResponse<String>> getCommitGeneralExchangeVolumeResponse(
            @Field("key") String key,
            @Field("exchange") String exchange
    );


    // 余额兑换通用卷
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_general_voucher&op=deposit_exchange")
    Observable<BalanceExchangeVolumeResponse> getBalanceExchangeVolumeResponseResponse(
            @Field("key") String key
    );

    // 余额兑换通用卷--提交
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_general_voucher&op=deposit_exchange_post")
    Observable<BaseResponse<String>> getBalanceExchangeVolumeResponse(
            @Field("key") String key,
            @Field("exchange") String exchange
    );

    // 我的订单-- ?act=member_order&op=order_list
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<MyOrderResponse> getMyOrderResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("state_type") String state_type,
            @Field("key") String key
    );

    // 订单详情-- ?act=member_order&op=order_info
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<OrderInfoResponse> getMyOrderDetailsResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Field("order_id") String order_id,
            @Field("key") String key
    );

    // 删除订单
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_order&op=order_del")
    Observable<BaseResponse<String>> getRemoveOrderResponse(
            @Field("order_id") String order_id,
            @Field("key") String key
    );

    // 取消订单
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_order&op=order_cancel")
    Observable<BaseResponse<String>> getCancelOrderResponse(
            @Field("order_id") String order_id,
            @Field("key") String key
    );

    // 订单评价提交-- mobile/index.php?act=member_evaluate&op=save
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_evaluate&op=save")
    Observable<BaseResponse<String>> getEvaluateResponse(
            @Field("order_id") String order_id,
            @Field("evaluate") String evaluate,
            @Field("key") String key
    );

    // 投诉商家-- mobile/index.php?act=member_complaint&op=complaint
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_complaint&op=complaint")
    Observable<BaseResponse<String>> getComplaintResponse(
            @Field("order_id") String order_id,
            @Field("content") String content,
            @Field("pic1") String pic1,
            @Field("pic2") String pic2,
            @Field("pic3") String pic3,
            @Field("pic4") String pic4,
            @Field("key") String key
    );


    // 帮助中心
    @FormUrlEncoded
    @POST("mobile/index.php?act=article&op=help_center")
    Observable<HelpCenterResponse> getHelpCenterResponse(
            @Field("key") String key
    );

    // 意见反馈
    @FormUrlEncoded
    @POST("mobile/index.php?act=consult&op=feedback")
    Observable<BaseResponse<String>> getFeedbackResponse(
            @Field("key") String key,
            @Field("content") String content,
            @Field("mobile") String mobile
    );

}
