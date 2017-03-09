package com.help.reward.network;

import com.help.reward.network.api.IntegrationApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class IntegrationNetwork {

    // 获取广告
    public static IntegrationApi getIntegrationApi(){
        return RetrofitUtils.getRetrofit().create(IntegrationApi.class);
    }

}
