package com.help.reward.bean;

/**
 * {
 * "voucher_id": "23",
 * "voucher_code": "642440552755901006",
 * "voucher_t_id": "11",
 * "voucher_title": "优惠券12",
 * "voucher_desc": "满100减10",
 * "voucher_start_date": "1499411901",
 * "voucher_end_date": "1501344000",
 * "voucher_price": "10.00",
 * "voucher_limit": "100.00",
 * "voucher_store_id": "1",
 * "voucher_state": "1",
 * "voucher_active_date": "1499411901",
 * "voucher_type": "0",
 * "voucher_owner_id": "6",
 * "voucher_owner_name": "zhouying01",
 * "voucher_order_id": null,
 * "voucher_from": "store",
 * "voucher_owner_setting": "0",
 * "desc": "面额10元 有效期至 2017-07-30 消费满100.00可用"
 * },
 * Created by wuxiaojun on 17-7-7.
 */

public class VoucherBean {

    public String voucher_id;
    public String voucher_code;
    public String voucher_t_id;
    public String voucher_title;
    public String voucher_desc;
    public String voucher_start_date;
    public String voucher_end_date;
    public String voucher_price;
    public String voucher_limit;

    public String voucher_state;
    public String voucher_active_date;
    public String voucher_type;
    public String voucher_owner_id;
    public String voucher_owner_name;
    public String voucher_order_id;
    public String voucher_from;
    public String voucher_owner_setting;
    public String desc;
    public String store_name; // 店铺名称
    public String voucher_store_id; // 店铺id
    public boolean useable; // 可用的
    public boolean isChecked; // 默认选中的

}
