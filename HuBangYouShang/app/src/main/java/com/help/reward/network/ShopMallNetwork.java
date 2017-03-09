package com.help.reward.network;

import com.help.reward.network.api.ShopMallApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public class ShopMallNetwork {

    // 登录api
    public static ShopMallApi getShopMallMainApi() {
        return RetrofitUtils.getRetrofit().create(ShopMallApi.class);
    }

}
