package com.help.reward.bean.Response;

import com.help.reward.bean.OrderInfoBean;

/**
 * Created by wuxiaojun on 2017/3/29.
 */

public class OrderInfoResponse extends BaseResponse<OrderInfoResponse.OrderData> {

    public class OrderData {

        public OrderInfoBean order_info;

    }

}
