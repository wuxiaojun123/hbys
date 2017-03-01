package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.BaseResponse;
import com.wxj.hbys.bean.Response.MyHelpPostResponse;

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

    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=my_seek_help")
    Observable<BaseResponse> getMyHelpPostResponse(
            @Field("type") String type,
            @Field("cookie") String cookie
    );

}
