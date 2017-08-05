package com.help.reward.bean.Response;

/**
 * "data": {
 * "out_trade_no": "830551982288737006",//商户订单号-支付单号
 * "subject": "实物订单_830551982288737006",//
 * "body": "测试内容",
 * "total_fee": "368.00",
 * "partner": "2088621553773591",
 * "seller_id": "2088621553773591",
 * "notify_url": "http://www.hubangyoushang.com/mobile/api/payment/alipay/notify_url.php",
 * "service": "mobile.securitypay.pay",
 * "payment_type": "1",
 * "_input_charset": "UTF-8",
 * "it_b_pay": "30m",
 * "sign_type": "RSA",
 * "sign": ""
 * }
 * <p>
 * Created by wuxiaojun on 17-6-16.
 */

public class PayTypeAlipayResponse extends BaseResponse<PayTypeAlipayResponse.PayTypeAlipayBean> {

    public class PayTypeAlipayBean {

        public String out_trade_no;
        public String subject;
        public String body;
        public String total_fee;
        public String partner;
        public String seller_id;
        public String notify_url;
        public String service;
        public String payment_type;
        public String _input_charset;
        public String it_b_pay;


        public String sign_type;
        public String sign;
        public String app_id;
        public String method;
        public String charset;
        public String timestamp;
        public String version;
        public String biz_content;


    }

}
