package com.help.reward.network.api;

import com.help.reward.bean.Response.AreaResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.HelpRewardsResponse;
import com.help.reward.bean.Response.HelpSeekCommentDetailResponse;
import com.help.reward.bean.Response.HelpSeekInfoResponse;
import com.help.reward.bean.Response.HelpSeekResponse;
import com.help.reward.bean.Response.HelpSubResponse;
import com.help.reward.bean.Response.HelpVoteInfoResponse;
import com.help.reward.bean.Response.HelpVoteResponse;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.bean.Response.SubVoteResponse;
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
    Observable<HelpSeekInfoResponse> getHelpSeekInfoBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id,
            @Field("curpage") int curpage
    );


    // 投票帖子详情op=vote_info
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpVoteInfoResponse> getVoteInfoBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id
    );


    // 投票op=vote_info
    @FormUrlEncoded
    @POST(Constant.URL_SUBVOTE)
    Observable<SubVoteResponse> getSubVoteBean(
            @Field("key") String key,
            @Field("complaint_id") String complaint_id,
            @Field("support") String support
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
            @Query("op") String op,
            @Field("board_id") String board_id,
            @Field("title") String title,
            @Field("content") String content,
            @Field("area_id") String area_id,
            @Field("end_time") String end_time,
            @Field("offer") String offer,
            @Field("image") String image

    );

    /**
     * 设置有用
     * 需要登录
     * /mobile/index.php?act=seek_help&op=set_useful
     * 参数[post]：parent_id 跟帖id
     * 踩
     * 需要登录
     * /mobile/index.php?act=seek_help&op=set_useless
     * 参数[post]：parent_id 跟帖id
     */
    @FormUrlEncoded
    @POST(Constant.URL_SUBSEEKHELP)
    Observable<StringResponse> getSetUseFulOrUselessBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("parent_id") String parent_id

    );

    /**
     * 最佳跟帖
     * 需要登录
     * /mobile/index.php?act=seek_help&op=set_best_comment
     * 参数[post]：post_id 帖子id ；comment_id 跟帖id
     */
    @FormUrlEncoded
    @POST(Constant.URL_SUBSEEKHELP)
    Observable<StringResponse> getSetBestCommentBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("comment_id") String comment_id

    );

    /**
     * 收藏帖子
     * /mobile/index.php?act=member_favorites_post&op=favorites_add
     * 参数[post]：post_id 帖子id；type：’help’/’reward’帖子类型
     */
    @FormUrlEncoded
    @POST(Constant.URL_FAVORITESADD)
    Observable<StringResponse> getFavoritesAddBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("type") String type

    );


    /**
     * 帖子评论
     * 需要登录
     * /mobile/index.php?act=seek_help&op=comment
     * 参数[post]：content 内容；post_id 帖子id；type 类型(1-私聊 2-公聊；无跟帖新跟帖时需要该参数)；
     * parent_id 跟帖id(一定是首个跟帖的评论id，下同)
     */
    @FormUrlEncoded
    @POST(Constant.URL_SUBSEEKHELP)
    Observable<StringResponse> getSubCommentBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("type") String type,
            @Field("parent_id") String parent_id,
            @Field("content") String content

    );

    /**
     * 给赏分
     * 需要登录
     * /mobile/index.php?act=seek_help&op=give_points
     * 参数[post]：points 分数(整数) comment_id 跟帖id
     */
    @FormUrlEncoded
    @POST(Constant.URL_SUBSEEKHELP)
    Observable<StringResponse> getGivePointsBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("comment_id") String comment_id,
            @Field("points") String points
    );


    /**
     * 投诉帖子
     * 需要登录
     * /mobile/index.php?act=member_p_complain&op=complain_shpost
     * 参数[post]：post_id 帖子id；type 帖子类型(help,admiration)；content 正文
     * 返回：投诉id
     */
    @FormUrlEncoded
    @POST(Constant.URL_COMPLAIN)
    Observable<BaseResponse> getComplainSHPostBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("comment_id") String comment_id,
            @Field("type") String type,
            @Field("content") String content
    );


    /**
     * 求助评论展开
     * 参数[post]：id 评论id
     * /mobile/index.php?act=index&op=comment_detail
     * “data”:[{评论1},{评论2}]
     */
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpSeekCommentDetailResponse> getSeekCommentDetailBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id
    );
}
