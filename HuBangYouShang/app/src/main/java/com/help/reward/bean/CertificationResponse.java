package com.help.reward.bean;

import com.help.reward.bean.Response.BaseResponse;

/**
 * "data": {
 * "member_id": "6",
 * "member_name": "zhouying",
 * "member_truename": "zy",
 * "certification": "0",
 * "ID_card": null,
 * "identity_img": null
 * }
 * Created by wuxiaojun on 2017/4/8.
 */

public class CertificationResponse extends BaseResponse<CertificationResponse.CertificationBean> {

    public class CertificationBean {
        public String member_truename;
        public String certification; // 0未认证 1通过 2未通过 3-审核中'
        public String ID_card; // 身份证
        public String identity_img; // 图片路径
    }

}
