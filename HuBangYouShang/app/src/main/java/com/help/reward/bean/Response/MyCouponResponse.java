package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardBean;
import com.help.reward.bean.MyCouponBean;

import java.util.List;

/**
 * 我的账户--帮赏分明细
 * Created by wuxiaojun on 17-3-15.
 */

public class MyCouponResponse extends BaseResponse<MyCouponResponse.MyCouponData> {

    public boolean hasmore;
    public int page_total;

    public class MyCouponData{
        public List<MyCouponBean> voucher_list;
        public String total_num;
    }

}
