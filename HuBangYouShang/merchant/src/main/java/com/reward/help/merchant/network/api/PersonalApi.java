package com.reward.help.merchant.network.api;


import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.UploadHeadImageReponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=member_index&op=my_seek_help
 * <p>
 * Created by wuxiaojun on 17-3-1.
 */

public interface PersonalApi {

    // mobile/index.php?act=logout 退出登录 post传client

    /*

    // 个人信息--获取地区
    @FormUrlEncoded
    @POST("mobile/index.php?act=area")
    Observable<AeraResponse> getAeraResponse(
            @Field("key") String key
    );

    // 个人信息---行业
    @FormUrlEncoded
    @POST("mobile/index.php?act=index&op=get_config")
    Observable<BusinessResponse> getBusinessResponse(
            @Field("config_name") String config_name
    );

    // 我的个人信息
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=member_info")
    Observable<PersonInfoResponse> getPersonInfoResponse(
            @Field("key") String key
    );

    // 我的个人信息--提交sex 0/1/2；business;position;description;avatar(上传后的url);province;city;area(省市区id);name
    @FormUrlEncoded
    @POST("mobile/index.php?act=member_index&op=edit_member_info")
    Observable<BaseResponse<String>> getUpdatePersonInfoResponse(
            @Field("key") String key,
            @Field("sex") String sex,
            @Field("business") String business,
            @Field("position") String position,
            @Field("description") String description,
            @Field("avatar") String avatar,
            @Field("province") String province,
            @Field("city") String city,
            @Field("area") String area,
            @Field("name") String name
    );

    // 上传头像
    @Multipart
    @POST("mobile/index.php?act=upload_file&op=upload_img")
    Observable<UploadHeadImageReponse> getUploadHeadImageReponse(
            // @Part("description") RequestBody description,
            @Part RequestBody file,
            @Query("type") String type,
            @Query("key") String key
    );
    */
    /**
     * 上传图片
     * @return
     */
    @Multipart
    @POST("mobile/index.php?act=upload_file&op=upload_img")
    Observable<UploadHeadImageReponse> uploadImage(
            @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part part);


    // 意见反馈
    @FormUrlEncoded
    @POST("mobile/index.php?act=consult&op=feedback")
    Observable<BaseResponse<String>> getFeedbackResponse(
            @Field("key") String key,
            @Field("content") String content,
            @Field("mobile") String mobile
    );

}
