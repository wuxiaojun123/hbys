package com.reward.help.merchant.network;


import com.reward.help.merchant.network.api.UploadImageApi;
import com.reward.help.merchant.network.base.RetrofitUtils;

/**
 *
 */

public class UploadImageNetwork {

    public static UploadImageApi getUploadApi() {
        return RetrofitUtils.getRetrofitCookie().create(UploadImageApi.class);
    }

}
