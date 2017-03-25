package com.reward.help.merchant.bean.Response;

import com.reward.help.merchant.bean.CouponLogBean;
import com.reward.help.merchant.bean.PointsLogBean;

/**
 * Created by fanjunqing on 25/03/2017.
 */

public class QueryPointsResponse extends BaseResponse<PointsLogBean> {

    public boolean hasmore;
    public int page_total;
}




