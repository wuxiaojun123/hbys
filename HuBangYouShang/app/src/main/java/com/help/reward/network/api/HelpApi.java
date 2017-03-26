package com.help.reward.network.api;

import com.help.reward.bean.Response.AreaResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.ComplaintStatusResponse;
import com.help.reward.bean.Response.HelpBoardResponse;
import com.help.reward.bean.Response.HelpComplainedDetailResponse;
import com.help.reward.bean.Response.HelpRewardChatDetailResponse;
import com.help.reward.bean.Response.HelpRewardChatResponse;
import com.help.reward.bean.Response.HelpRewardCommentResponse;
import com.help.reward.bean.Response.HelpRewardInfoResponse;
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
            @Field("title") String title,
            @Field("board_id") String board_id,
            @Field("area_id") String area_id,
            @Query("curpage") int curpage
    );

    //op=get_reward  获赏贴列表
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpRewardsResponse> getHelpRewardsBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("title") String title,
            @Field("board_id") String board_id,
            @Field("area_id") String area_id,
            @Query("curpage") int curpage
    );

    //op=complaint  获赏投票表
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpVoteResponse> getHelpVoteBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("title") String title,
            @Field("board_id") String board_id,
            @Field("area_id") String area_id,
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

    /**
     * 获赏贴详情
     * /mobile/index.php?act=index&op=get_reward_detail
     * 参数[post]：id页数参数此处分别为curpage(评论的) 和curpage2(私聊的)，
     * 返回：
     * "hasmore": false,"page_total": 1,//评论分页
     * "hasmore2": false,"page_total2": 1,//私聊分页。
     * ,”data”:{‘info’:’admiration_list是赞赏名单，暂最多16人，admiration为全部赞人数’,’chat_list’:’私聊，有分页’,’message_list’:’评论，有分页。’}
     */
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpRewardInfoResponse> getHelpRewardInfoBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id
    );

    /**
     * 获赏贴详情 评论列表
     * /mobile/index.php?act=index&op=reward_comment
     * 参数[post]：id
     * 返回：
     * "hasmore": false,"page_total": 1,//评论分页
     * message_list  type=2 avatar content id u_name creat_time
     */
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpRewardCommentResponse> getHelpRewardCommentBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id,
            @Field("curpage") int curpage
    );

    /**
     * 获赏贴详情 私聊列表
     * /mobile/index.php?act=index&op=reward_prvChat
     * 参数[post]：id
     * 返回：
     * "hasmore": false,"page_total": 1,//评论分页
     * message_list  type=2 avatar content id u_name creat_time
     */
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpRewardChatResponse> getHelpRewardChatBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id,
            @Field("curpage") int curpage
    );

    /**
     * 打赏 获赏帖
     * /mobile/index.php?act=get_reward&op=reward_post
     * 参数[get]：获赏贴id
     * 打赏成功失败以code200/201为准
     */
    @FormUrlEncoded
    @POST(Constant.URL_GETREWARD)
    Observable<StringResponse> getGiveRewardPoints10Bean(
            @Field("key") String key,
            @Query("op") String op,
            @Query("id") String id
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
     * 获取板块列表
     * /mobile/index.php?act=index&op=get_board
     * "data": {
     * "board_list": [
     * {
     * "id": "1",
     * "board_name": "创新创业"
     * },
     * …]}
     */
    @FormUrlEncoded
    @POST(Constant.URL_BOARD)
    Observable<HelpBoardResponse> getBoardBean(
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
            @Field("url") String[] url

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
            @Field("url") String[] url

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
            @Field("content") String content,
            @Field("url") String[] url
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

    /**
     * 获赏评论展开
     * /mobile/index.php?act=index&op=reward_commment_expand
     * 参数[post]：id 私聊id
     */
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<HelpRewardChatDetailResponse> getRewardCommentDetailBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id
    );


    /**
     * 获赏贴私聊提交
     * /mobile/index.php?act=get_reward&op=private_chat
     * 参数[post]：content 内容；post_id 帖子id；
     */
    @FormUrlEncoded
    @POST(Constant.URL_GETREWARD)
    Observable<StringResponse> getSubRewardChatBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("parent_id") String parent_id,
            @Field("content") String content

    );


    /**
     * 获赏贴评论
     * /mobile/index.php?act=get_reward&op=comment
     * 参数[post]：content 内容；post_id 帖子id；
     */
    @FormUrlEncoded
    @POST(Constant.URL_GETREWARD)
    Observable<StringResponse> getSubRewardCommentBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("content") String content
    );


    /**
     * 获赏贴单独给赏分
     * /mobile/index.php?act=get_reward&op=give_points
     * 参数[post]：points 分数；post_id;
     */
    @FormUrlEncoded
    @POST(Constant.URL_GETREWARD)
    Observable<StringResponse> getGiveRewardPointsBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("post_id") String post_id,
            @Field("points") String points
    );


    /**
     * 投诉信息页
     * 需要登录，申诉和解释时
     * /mobile/index.php?act=member_p_complain&op=info
     * 参数[post]：complaint_id 投诉id
     * "data": {
     * "id": "2",
     * "complainant_id": "1",//原告、投诉人id
     * "complainant_name": "mahuateng",//投诉人姓名
     * "complainant_vote": "0",
     * "respondent_id": "1",	//被投诉者id
     * "respondent_name": "mahuateng",//被投诉者姓名
     * "respondent_vote": "0",
     * "post_id": "1",//关联帖子id
     * "post_type": "help",//关联帖子类型
     * "post_title": "",//关联帖子标题
     * "post_board": "创新创业",
     * "create_time": "1484731833",
     * "content": "我疯啦，我投诉自己啦",//投诉理由
     * "appeal": "阳光在手指尖。",//申诉理由
     * "appeal_time": "1484793515",
     * "complainant_explain": null,//投诉人解释
     * "com_explain_time": null,
     * "respondent_explain": "大家好，我是MC子龙。",//被投诉者解释
     * "respondent_time": "1484796441",
     * "update_time": "1484796441",
     * "status": "已申诉",//状态'和解','已结束','投票中','待申诉','已申诉'
     * "result": null
     * }
     */
    @FormUrlEncoded
    @POST(Constant.URL_COMPLAIN)
    Observable<HelpComplainedDetailResponse> getComplainedDetailBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("complaint_id") String complaint_id
    );

    /**
     * 申诉提交
     * 需要登录
     * /mobile/index.php?act=member_p_complain&op=appeal
     * 参数[post]：complaint_id投诉id；content 申诉内容
     * 解释提交
     * 需要登录
     * /mobile/index.php?act=member_p_complain&op=explain
     * 参数[post]：complaint_id投诉id；content 申诉内容
     */
    @FormUrlEncoded
    @POST(Constant.URL_COMPLAIN)
    Observable<BaseResponse> getSubComplainedBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("complaint_id") String complaint_id,
            @Field("content") String content,
            @Field("url") String[] url
    );


    /**
     * 投诉信息页
     * 需要登录，申诉和解释时
     * /mobile/index.php?act=index&op=complaint_status
     * 参数[post]：id 投诉id
     * "status": '和解','已结束','投票中' 挑投票     待申诉','已申诉'跳填东西
     */
    @FormUrlEncoded
    @POST(Constant.URL_HELP)
    Observable<ComplaintStatusResponse> getComplaintStatusBean(
            @Field("key") String key,
            @Query("op") String op,
            @Field("id") String id
    );


}
