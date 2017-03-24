package com.help.reward.bean.Response;

import com.help.reward.bean.MemberInfoBean;

/**
 * 会员信息
 * Created by wuxiaojun on 2017/3/23.
 */

public class MemberInfoResponse extends BaseResponse<MemberInfoResponse.MemberInfoData> {

    public class MemberInfoData {
        public MemberInfoBean member_info;
    }

}
