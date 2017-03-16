package com.help.reward.bean.Response;

/**
 * 余额兑换通用卷
 * "data": {
 * "available_total_num": 110.06,
 * "deposit_has_exchanged": "1",
 * "rest_could_exchange": 1999,
 * "general_voucher": "1.15"
 * }
 * Created by wuxiaojun on 2017/3/16.
 */

public class BalanceExchangeVolumeResponse extends BaseResponse<BalanceExchangeVolumeResponse.BalanceExchangeVolumeBean> {

    public class BalanceExchangeVolumeBean {

        public float available_total_num; // 总余额
        public String deposit_has_exchanged; // 兑换公式
        public int rest_could_exchange; //
        public String general_voucher;

    }

}
