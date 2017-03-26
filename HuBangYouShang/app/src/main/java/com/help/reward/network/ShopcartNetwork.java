package com.help.reward.network;

import com.help.reward.network.api.ShopcartApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 * Created by ADBrian on 26/03/2017.
 */

public class ShopcartNetwork {

    public static ShopcartApi getShopcartCookieApi() {
        return RetrofitUtils.getRetrofitCookie().create(ShopcartApi.class);
    }
}
