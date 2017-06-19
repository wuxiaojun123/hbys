package com.help.reward.bean;

import java.util.List;

/**
 * Created by wuxiaojun on 2017/3/29.
 */

public class OrderInfoBean {

    public String order_sn; // 订单号
    public String pay_sn; // 支付单号
    public String store_name; // 商家名称
    public String add_time; // 下单时间
    public String lock_state; // 锁的状态
    public String order_state; // 订单状态
    public String state_desc; // 订单状态--直接显示
    public String shipping_code; // 物流单号
    public String shipping_fee; // 运费
    public String payment_name; // 支付方式
    public String real_pay_amount; // 实际付款
    public String goods_amount; // 商品总价
    public String order_amount; // 订单总价
    public String store_phone; // 商家电话
    public String evaluation_state;// 评价状态 0未评价，1已评价，2已过期未评价

    public Extend_order_common extend_order_common; //
    public List<MyOrderShopBean> goods_list; // 商品列表
    public Extend_store extend_store; // 商家信息

    public class Extend_order_common {
        public String reciver_name; // 收货人姓名
        public reciver_info reciver_info; // 收货人信息
        public String voucher_price; // 优惠劵满减
        public String dlyo_pickup_code; // 消费验证码

    }

    public class reciver_info {
        public String mob_phone;
        public String tel_phone;
        public String address;
    }

    public class Extend_store{
        public String live_store_tel; // 商家电话
        public String live_store_address; // 商家地址
    }

}
