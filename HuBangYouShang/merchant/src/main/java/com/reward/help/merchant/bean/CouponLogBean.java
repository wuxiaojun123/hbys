package com.reward.help.merchant.bean;

import java.util.List;

/**
 * Created by fanjunqing on 25/03/2017.
 */

public class CouponLogBean {

    public List<Member> receive_log_list;

    public VoucherInfo voucher_info;

    public class VoucherInfo{
        public String id;
        public String seller_id;
        public String seller_name;
        public String store_id;
        public String voucher_tpl_id;
        public String num;
        public String num_given;
        public String created;
        public String voucher_t_price;
        public String voucher_t_limit;
        public String avatar;
    }

    public class Member{
        public String member_id;
        public String member_name;
        public String created;
        public String member_avatar;
    }
}