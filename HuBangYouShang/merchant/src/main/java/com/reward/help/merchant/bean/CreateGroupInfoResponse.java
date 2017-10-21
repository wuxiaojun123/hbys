package com.reward.help.merchant.bean;

import com.reward.help.merchant.bean.Response.BaseResponse;

/**
 * "data": {
 * "info": {
 * "id": "40",
 * "seller_id": "1",
 * "content": "再测",
 * "created": "1508117630",
 * "status": "正在审核",
 * "explain": null,
 * "group_name": "帮帮忙商家群2"
 * }
 * }
 * Created by wuxiaojun on 2017/10/21.
 */

public class CreateGroupInfoResponse extends BaseResponse<CreateGroupInfoResponse.CreateGroupInfoClass> {

    public class CreateGroupInfoClass {
        public CreateGroupInfoBean info;
    }

    public class CreateGroupInfoBean {
        public String id;
        public String seller_id;
        public String content;
        public String created;
        public String status;
        public String explain;
        public String group_name;
    }

}
