package com.help.reward.network;

import com.help.reward.network.api.StoreApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public class StoreDetailNetwork {

    public static StoreApi getStroeApi() {
        return RetrofitUtils.getRetrofit().create(StoreApi.class);
    }

}
