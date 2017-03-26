package com.help.reward.bean.Response;

import com.help.reward.bean.MyOrderListBean;

import java.util.List;

/**
 * 我的订单
 * Created by wuxiaojun on 2017/3/25.
 */

public class MyOrderResponse extends BaseResponse<MyOrderResponse.MyOrderData>{

    public String page_total; // 总页数
    public boolean hasmore; // 是否有更多

    public class MyOrderData{

        public List<MyOrderListBean> order_group_list;

    }

}
