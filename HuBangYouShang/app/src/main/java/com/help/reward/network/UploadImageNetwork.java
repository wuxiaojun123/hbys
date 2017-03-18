package com.help.reward.network;

import com.help.reward.network.api.UploadImageApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 *
 */

public class UploadImageNetwork {

    public static UploadImageApi getUploadApi() {
        return RetrofitUtils.getRetrofitCookie().create(UploadImageApi.class);
    }

}
