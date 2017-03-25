package com.reward.help.merchant.bean.Response;

import com.reward.help.merchant.bean.CouponLogBean;
import com.reward.help.merchant.bean.PointsLogBean;

public class QueryPointsResponse extends BaseResponse<PointsLogBean> {

    public boolean hasmore;
    public int page_total;
}




