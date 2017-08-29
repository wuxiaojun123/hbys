package com.help.reward.bean.Response;

import java.util.List;

/**
 * 支付页选择支付方式
 * Created by wuxiaojun on 17-6-16.
 */

public class PrepaidBalanceResponse extends BaseResponse<PrepaidBalanceResponse.PayTypeBean> {

    public class PayTypeBean {
        public String pay_sn; // 支付单号
        public String api_pay_amount; // 总价/支付价
    }

    /*public class PayTypeOrderBean {
        public String order_sn; // 订单编码
        public String order_amount; // 订单金额

    }*/

}
