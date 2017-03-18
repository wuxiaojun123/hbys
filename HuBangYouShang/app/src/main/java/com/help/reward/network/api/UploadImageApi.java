package com.help.reward.network.api;

import com.help.reward.bean.Response.UploadImageResponse;
import com.help.reward.utils.Constant;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * http://210.72.13.135/mobile/index.php?act=upload_file&op=upload_img
 */

public interface UploadImageApi {

    @Multipart
    @POST(Constant.URL_UPLOADIMAGE)
    Observable<UploadImageResponse> getUploadBean(
            @Field("key") String key,
            @Field("type") String type,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

}
