package com.wxj.hbys.network;

import com.wxj.hbys.network.api.IntegrationApi;
import com.wxj.hbys.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class IntegrationNetwork {

    // 获取广告
    public static IntegrationApi getIntegrationApi(){
        return RetrofitUtils.getRetrofit().create(IntegrationApi.class);
    }

}
