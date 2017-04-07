package com.help.reward.bean.Response;

/**
 * Created by ADBrian on 07/04/2017.
 */

public class CommitOrderResponse extends BaseResponse<CommitOrderResponse.CommitOrderBean> {


    public class CommitOrderBean{
        public String pay_sn;
        public String payment_code;
    }
}
