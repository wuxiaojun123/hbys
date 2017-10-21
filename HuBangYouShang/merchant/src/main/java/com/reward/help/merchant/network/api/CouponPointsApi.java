package com.reward.help.merchant.network.api;

import com.reward.help.merchant.bean.CreateGroupInfoResponse;
import com.reward.help.merchant.bean.GroupCouponsRecordResponse;
import com.reward.help.merchant.bean.GroupToStoreResponse;
import com.reward.help.merchant.bean.HuanXinUserInfo;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.CouponListResponse;
import com.reward.help.merchant.bean.Response.GroupGrantHelpPointsResponse;
import com.reward.help.merchant.bean.Response.GroupProgressResponse;
import com.reward.help.merchant.bean.Response.QueryCouponResponse;
import com.reward.help.merchant.bean.Response.QueryMyPointsResponse;
import com.reward.help.merchant.bean.Response.QueryPointsResponse;
import com.reward.help.merchant.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 优惠券和积分
 */

public interface CouponPointsApi {

    // 登录
    @FormUrlEncoded
    @POST(Constant.URL_GET_COUPON_LIST)
    Observable<CouponListResponse> getCouponList(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_SEND_COUPON)
    Observable<BaseResponse> sendCoupon(
            @Field("key") String key,
            @Field("voucher_t_id") String voucher_t_id,
            @Field("num") String num,
            @Field("groupid") String groupid
    );

    @FormUrlEncoded
    @POST(Constant.URL_QUERY_COUPON)
    Observable<QueryCouponResponse> queryCoupon(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    @FormUrlEncoded
    @POST(Constant.URL_SEND_POINTS)
    Observable<BaseResponse> sendPoints(
            @Field("key") String key,
            @Field("people_limit") String people_limit,
            @Field("num_limit") String num_limit,
            @Field("groupid") String groupid
    );

    @FormUrlEncoded
    @POST(Constant.URL_QUERY_POINTS)
    Observable<QueryPointsResponse> queryPoints(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    @FormUrlEncoded
    @POST(Constant.URL_GET_POINTS)
    Observable<QueryMyPointsResponse> getPoints(
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_GROUP_APPLY)
    Observable<BaseResponse> apply(
            @Field("key") String key,
            @Field("content") String content
    );


    @FormUrlEncoded
    @POST(Constant.URL_GROUP_PROGRESS)
    Observable<GroupProgressResponse> queryApplyProgress(
            @Field("key") String key
    );

    /***
     * 获取建群申请的信息
     * @param id
     * @param key
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/index.php?act=seller_group&op=groupApplyInfo")
    Observable<CreateGroupInfoResponse> getApplyProgressInfo(
            @Field("id") String id,
            @Field("key") String key
    );

    /***
     * 提交修改建群信息
     * @param id
     * @param key
     * @return
     */
    @FormUrlEncoded
    @POST("/mobile/index.php?act=seller_group&op=groupApplyEdit")
    Observable<BaseResponse<String>> commitModifyApplyProgress(
            @Field("id") String id,
            @Field("content") String content,
            @Field("key") String key
    );

    @FormUrlEncoded
    @POST(Constant.URL_GROUP_STORE)
    Observable<GroupToStoreResponse> getStoreId(
            @Field("key") String key,
            @Field("groupId") String groupId
    );

    // 群发帮赏分发放记录
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<GroupGrantHelpPointsResponse> groupGrantHelpPointsLog(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("groupId") String groupId,
            @Field("key") String key
    );

    // 群发优惠劵发放记录
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<GroupCouponsRecordResponse> groupGrantCouponsLog(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("groupId") String groupId,
            @Field("key") String key
    );

    // 根据环信id获取用户昵称和头像
    @FormUrlEncoded
    @POST("mobile/index.php?act=index&op=getEaseMobInfo")
    Observable<HuanXinUserInfo> getUserNickAndHead(
//            @Field("key") String key,
            @Field("easeMobId") String easeMobId
    );

    // 批量删除群成员
    @FormUrlEncoded
    @POST("/mobile/index.php?act=seller_group&op=removeGroupUser")
    Observable<BaseResponse<String>> removeGroupMembers(
            @Field("groupId") String groupId,
            @Field("usernames") String usernames,
            @Field("key") String key
    );

}