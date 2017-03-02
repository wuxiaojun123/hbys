package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.BaseResponse;
import com.wxj.hbys.bean.Response.MyHelpCommentResponse;
import com.wxj.hbys.bean.Response.MyHelpPostResponse;
import com.wxj.hbys.bean.Response.MyRewardCommentResponse;
import com.wxj.hbys.bean.Response.MyRewardPostResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=member_index&op=my_seek_help
 *
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

}
