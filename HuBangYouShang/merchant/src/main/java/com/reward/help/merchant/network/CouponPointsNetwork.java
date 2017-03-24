package com.reward.help.merchant.network;

import com.reward.help.merchant.network.api.CouponPointsApi;
import com.reward.help.merchant.network.base.RetrofitUtils;

/**
 * Created by fanjunqing on 24/03/2017.
 */

public class CouponPointsNetwork {
    public static CouponPointsApi getCouponListApi() {
        return RetrofitUtils.getRetrofit().create(CouponPointsApi.class);
    }
}
