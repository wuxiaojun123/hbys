package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.BaseResponse;
import com.wxj.hbys.bean.Response.MyCollectionGoodsResponse;
import com.wxj.hbys.bean.Response.MyCollectionPostResponse;
import com.wxj.hbys.bean.Response.MyCollectionStoreResponse;
import com.wxj.hbys.bean.Response.MyHelpCommentResponse;
import com.wxj.hbys.bean.Response.MyHelpPostResponse;
import com.wxj.hbys.bean.Response.MyRewardCommentResponse;
import com.wxj.hbys.bean.Response.MyRewardPostResponse;
import com.wxj.hbys.bean.Response.MyVoteResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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

}
