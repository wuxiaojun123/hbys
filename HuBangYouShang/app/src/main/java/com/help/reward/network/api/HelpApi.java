package com.help.reward.network.api;

import com.help.reward.bean.Response.AreaResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.HelpRewardsResponse;
import com.help.reward.bean.Response.HelpSeekResponse;
import com.help.reward.bean.Response.HelpSubResponse;
import com.help.reward.bean.Response.HelpVoteResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=index
 */

public interface HelpApi {

    //求助帖列表op=seek_help
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpSeekResponse> getHelpSeekBean(
            @Field("key") String key,
            @Query("op") String op,
            @Query("curpage") int curpage
    );

    //op=get_reward  获赏贴列表
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpRewardsResponse> getHelpRewardsBean(
            @Field("key") String key,
            @Query("op") String op,
            @Query("curpage") int curpage
    );

    //op=complaint  获赏投票表
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpVoteResponse> getHelpVoteBean(
            @Field("key") String key,
            @Query("op") String op,
            @Query("curpage") int curpage
    );

    // 求助帖子详情op=seek_help_detail
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<BaseResponse> clearMessageBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id
    );

    // 发帖地址选择
    @FormUrlEncoded
    @POST(Constant.URL_AREA)
    Observable<AreaResponse> getAreaBean(
            @Field("key") String key

    );

    /**
     * 发布获赏贴
     * 参数[post]：board_id 板块id；title 标题；content 正文；area_id 可见区域id
     * 返回：帖子id
     */
    @FormUrlEncoded
    @POST(Constant.URL_SUBREWARD)
    Observable<HelpSubResponse> subHelpRewardBean(
            @Field("key") String key,
            @Field("board_id") String board_id,
            @Field("title") String title,
            @Field("content") String content,
            @Field("area_id") String area_id,
            @Field("offer") String offer,
            @Field("image") String image

    );

    /**
     * 发布求助帖
     * 需要登录
     * 参数[post]：end_time 时间格式；board_id 板块id；offer 悬赏分；title 标题；content 正文；area_id 可见区域id
     * 返回：帖子id
     */

    @FormUrlEncoded
    @POST(Constant.URL_SUBSEEKHELP)
    Observable<HelpSubResponse> subHelpSeekBean(
            @Field("key") String key,
            @Field("board_id") String board_id,
            @Field("title") String title,
            @Field("content") String content,
            @Field("area_id") String area_id,
            @Field("end_time") String end_time,
            @Field("offer") String offer,
            @Field("image") String image

    );


}
