package com.help.reward.network.api;

import com.help.reward.bean.Response.AdvertisementResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.GeneralVolumeResponse;
import com.help.reward.bean.Response.HelpRewardResponse;
import com.help.reward.bean.Response.MyCollectionGoodsResponse;
import com.help.reward.bean.Response.MyCollectionPostResponse;
import com.help.reward.bean.Response.MyCollectionStoreResponse;
import com.help.reward.bean.Response.MyHelpCommentResponse;
import com.help.reward.bean.Response.MyHelpPostResponse;
import com.help.reward.bean.Response.MyRewardCommentResponse;
import com.help.reward.bean.Response.MyRewardPostResponse;
import com.help.reward.bean.Response.MyVoteResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=member_index&op=my_seek_help
 * <p>
 * Created by wuxiaojun on 17-3-1.
 */

public interface PersonalApi {

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

    // 我的账户--我的优惠卷  ?act=member_general_voucher&op=detail_lo
    @FormUrlEncoded
    @POST(Constant.URL_MEMBER_ACCOUNT_GENERAL_VOLUME)
    Observable<GeneralVolumeResponse> getMyGeneralVolumeResponse(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key,
            @Field("type") String type
    );

}
