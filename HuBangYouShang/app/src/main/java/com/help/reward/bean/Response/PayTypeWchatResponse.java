package com.help.reward.bean.Response;

/**
 * "data": {
 * "appid": "wx5a5e4c632b2ae894",
 * "noncestr": "bbb0ceedba3ade2baf4b98f823b51329",
 * "package": "Sign=WXPay",
 * "partnerid": "1473770802",
 * "prepayid": "wx20170616104304607d6cfd2a0369545459",//预支付id
 * "timestamp": 1497580984,
 * "sign": "5A9B207CFC1468EC503E563042E590BD"//签名
 * }
 * Created by wuxiaojun on 17-6-16.
 */

public class PayTypeWchatResponse extends BaseResponse<PayTypeWchatResponse.PayTypeWchatBean> {

    public class PayTypeWchatBean {

        public String appid;
        public String noncestr;
        public String packagestr;
        public String partnerid;
        public String prepayid;
        public String timestamp;
        public String sign;

    }

}
