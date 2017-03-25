package com.reward.help.merchant.network;

import com.reward.help.merchant.network.api.CouponPointsApi;
import com.reward.help.merchant.network.base.RetrofitUtils;

public class CouponPointsNetwork {
    /**
     * 优惠券
     * @return
     */
    public static CouponPointsApi getCouponListApi() {
        return RetrofitUtils.getRetrofitCookie().create(CouponPointsApi.class);
    }
}
