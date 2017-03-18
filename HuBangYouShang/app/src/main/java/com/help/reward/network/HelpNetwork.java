package com.help.reward.network;

import com.help.reward.network.api.HelpApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 *
 */

public class HelpNetwork {

    public static HelpApi getHelpApi() {
        return RetrofitUtils.getRetrofitCookie().create(HelpApi.class);
    }

}
