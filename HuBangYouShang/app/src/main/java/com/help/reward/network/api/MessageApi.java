package com.help.reward.network.api;

import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.MessageReadResponse;
import com.help.reward.bean.Response.MessageResponse;
import com.help.reward.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=member_message&op=message
 * 参数[post]：type 0为私信、1为系统消息、2为留言、3帖子动态、4账户消息、5交易信息、
 */

public interface MessageApi {

    //op=message
    @FormUrlEncoded
    @POST(Constant.URL_MESSAGE)
    Observable<MessageResponse> getMessageBean(
            @Field("key") String key,
            @Field("type") String type,
            @Query("op") String op,
            @Query("curpage") int curpage
    );

    //op=new_msg_num 未读消息数量
    @FormUrlEncoded
    @POST(Constant.URL_MESSAGE)
    Observable<MessageReadResponse> getMessageReadBean(
            @Field("key") String key,
            @Query("op") String op
    );

    // 清空消息op=clear_up
    @FormUrlEncoded
    @POST(Constant.URL_MESSAGE)
    Observable<BaseResponse> clearMessageBean(
            @Field("key") String key,
            @Field("type") String type,
            @Query("op") String op
    );

    // 删除消息op=dropcommonmsg
    @FormUrlEncoded
    @POST(Constant.URL_MESSAGE)
    Observable<BaseResponse> deleteMessageBean(
            @Field("key") String key,
            @Field("message_id") String message_id,
            @Query("op") String op
    );

}
