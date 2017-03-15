package com.help.reward.bean.Response;

/**
 * "available_total_num": 113.06,
 "multiple": "2",
 "member_points": "343"
 * Created by wuxiaojun on 2017/3/16.
 */

public class BalanceExchangeResponse extends BaseResponse<BalanceExchangeResponse.BalanceExchangeBean>{

    public class BalanceExchangeBean{

        public float available_total_num; // 总余额
        public String multiple; // 兑换公式
        public String member_points; // 剩余帮赏分

    }

}
