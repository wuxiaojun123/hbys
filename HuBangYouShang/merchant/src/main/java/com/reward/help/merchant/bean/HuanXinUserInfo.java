package com.reward.help.merchant.bean;

import com.reward.help.merchant.bean.Response.BaseResponse;

/**
 * Created by wuxiaojun on 17-8-31.
 */

public class HuanXinUserInfo extends BaseResponse<HuanXinUserInfo.HuanXinMemberInfo> {

    public class HuanXinMemberInfo{
        public String member_id;
        public String member_avatar;
        public String nick_name;

    }

}
