package com.reward.help.merchant.bean.Response;


import com.reward.help.merchant.bean.CouponListBean;

import java.util.List;

public class CouponListResponse extends BaseResponse<CouponListResponse.CouponListBeans> {

    public class CouponListBeans{
        public List<CouponListBean> voucher_list;
    }
}
