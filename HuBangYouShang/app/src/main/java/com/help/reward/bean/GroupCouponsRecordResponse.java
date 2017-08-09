package com.help.reward.bean;

import com.help.reward.bean.Response.BaseResponse;

import java.util.List;

/**
 * Created by wuxiaojun on 17-8-9.
 */

public class GroupCouponsRecordResponse extends BaseResponse<GroupCouponsRecordResponse.GroupCouponsRecord> {

    public boolean hasmore;
    public int page_total;

    public class GroupCouponsRecord {
        public List<GroupCouponBean> giveList;
    }

}