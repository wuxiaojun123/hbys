package com.help.reward.bean;

import java.util.List;

/**
 * Created by ADBrian on 03/06/2017.
 */

public class CouponRecordBean {
    public String created;
    public GiveInfo give_info;
    public TplInfo tpl_info;

    public class GiveInfo{
        public String num;
        public String num_given;
        public String created;
    }

    public class TplInfo{
        public String voucher_t_price;
        public String voucher_t_limit;
    }
}
