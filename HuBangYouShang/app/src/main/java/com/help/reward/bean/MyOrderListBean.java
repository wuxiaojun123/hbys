package com.help.reward.bean;

import java.util.List;

/**
 * Created by wuxiaojun on 2017/3/25.
 */

public class MyOrderListBean {

    public List<OrderList> order_list;

    public class OrderList{

        public String order_id; // 订单id
        public String order_amount; // 订单总价
        public String shipping_fee; // 运费
        public String evaluation_state;// 评价状态 0未评价，1已评价，2已过期未评价
        public String order_state; // 订单状态

        public String store_name; // 店铺名称
        public List<MyOrderShopBean> extend_order_goods; // 商品集合
    }

}
