package com.help.reward.bean.Response;

import com.help.reward.bean.CouponTradingBean;
import com.help.reward.bean.MyCouponBean;

import java.util.List;

/**
 * 我的账户--优惠劵交易大厅
 * Created by wuxiaojun on 17-3-15.
 */

public class CouponTradingResponse extends BaseResponse<CouponTradingResponse.MyCouponData> {

    public boolean hasmore;
    public int page_total;

    public class MyCouponData{
        public List<CouponTradingBean> voucher_list;
    }

}
