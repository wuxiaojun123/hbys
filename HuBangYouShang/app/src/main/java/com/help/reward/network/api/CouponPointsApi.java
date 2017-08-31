package com.help.reward.network.api;


import com.help.reward.bean.GroupCouponsRecordResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.CouponsRecordResponse;
import com.help.reward.bean.Response.GroupGrantHelpPointsResponse;
import com.help.reward.bean.Response.GroupToStoreResponse;
import com.help.reward.bean.Response.HuanXinUserInfo;
import com.help.reward.bean.Response.PointsRecordResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface CouponPointsApi {

    @FormUrlEncoded
    @POST(Constant.URL_RECEIVE_COUPON)
    Observable<BaseResponse> receiveCoupon(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    @FormUrlEncoded
    @POST(Constant.URL_RECEIVE_POINTS)
    Observable<BaseResponse> receivePoints(
            @Field("key") String key,
            @Field("give_log_id") String give_log_id
    );

    // 个人领取帮赏分发放记录
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<PointsRecordResponse> receivePointsLog(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key
    );

    // 个人领取优惠劵发放记录
    @FormUrlEncoded
    @POST("mobile/index.php")
    Observable<CouponsRecordResponse> receiveCouponsLog(
            @Query("act") String act,
            @Query("op") String op,
            @Query("curpage") String curpage,
            @Field("key") String key
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

    @FormUrlEncoded
    @POST(Constant.URL_GROUP_STORE)
    Observable<GroupToStoreResponse> getStoreId(
            @Field("key") String key,
            @Field("groupId") String groupId
    );

    // 根据环信id获取用户昵称和头像
    @FormUrlEncoded
    @POST("mobile/index.php?act=index&op=getEaseMobInfo")
    Observable<HuanXinUserInfo> getUserNickAndHead(
//            @Field("key") String key,
            @Field("easeMobId") String easeMobId
    );

}
