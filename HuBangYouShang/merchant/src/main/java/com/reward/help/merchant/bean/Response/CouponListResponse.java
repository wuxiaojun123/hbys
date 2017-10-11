package com.reward.help.merchant.bean.Response;


import com.reward.help.merchant.bean.CouponListBean;

import java.util.List;

public class CouponListResponse extends BaseResponse<CouponListResponse.CouponListBeans> {


    public boolean hasmore;

    public int page_total;

    public class CouponListBeans{
        public List<CouponListBean> voucher_list;
    }
}


