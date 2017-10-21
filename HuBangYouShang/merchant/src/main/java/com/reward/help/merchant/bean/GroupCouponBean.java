package com.reward.help.merchant.bean;

/**
 * 群发放优惠劵实体类
 * <p>
 * "num": "3",//发放数
 * "num_given": "3",//领取数
 * "created": "1497612395",//发放时间
 * "tpl_info": {
 * "voucher_t_price": "10",//优惠券减免
 * "voucher_t_limit": "200.00"//优惠券满额
 * }
 * <p>
 * Created by wuxiaojun on 17-8-9.
 */

public class GroupCouponBean {

    public String num;
    public String num_given;
    public String created;
    public TplInfo tpl_info;

    public class TplInfo {
        public String voucher_t_price;
        public String voucher_t_limit;
    }

}
