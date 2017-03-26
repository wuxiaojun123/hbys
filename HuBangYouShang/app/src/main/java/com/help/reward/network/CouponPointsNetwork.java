package com.help.reward.network;

import com.help.reward.network.api.CouponPointsApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 * Created by ADBrian on 26/03/2017.
 */

public class CouponPointsNetwork {

    public static CouponPointsApi getHelpNoCookieApi() {
        return RetrofitUtils.getRetrofitCookie().create(CouponPointsApi.class);
    }
}
