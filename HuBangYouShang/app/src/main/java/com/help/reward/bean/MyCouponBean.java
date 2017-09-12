package com.help.reward.bean;

/**
 * {
 * "voucher_id": "2",
 * "voucher_code": "783000531917446001",
 * "voucher_title": "我有付钱吗？",
 * "voucher_desc": "什么鬼？",
 * "voucher_start_date": "1478573446",
 * "voucher_end_date": "1481040000",
 * "voucher_price": "100",
 * "voucher_limit": "299.00",
 * "voucher_state": "3",
 * "voucher_order_id": null,
 * "voucher_store_id": "2",
 * "store_name": "我知道",
 * "store_id": "2",
 * "store_domain": null,
 * "voucher_t_customimg": "http://210.72.13.135/data/upload/shop/voucher/2/05319173249000986_small.jpg",
 * "voucher_state_text": "已过期"
 * }
 * Created by wuxiaojun on 2017/3/15.
 */

public class MyCouponBean {

    public String voucher_id;
    public String voucher_code;
    public String voucher_title;
    public String voucher_desc;
    public String voucher_start_date;
    public String voucher_end_date;
    public String voucher_price;
    public String voucher_limit;
    public String voucher_state;
    public String voucher_order_id;
    public String voucher_store_id;
    public String store_name;
    public String store_id;
    public String store_domain;
    public String voucher_t_customimg;
    public String voucher_state_text;
    public String voucher_from; // 表明优惠劵是买来的还是领取的，store 就是表示是领的，user表示就是买来的

}
