package com.help.reward.bean.Response;

/**
 * {
 * "member_points": "190",//帮赏分
 * "discount_level": "10",//优惠百分比
 * "general_voucher": "22.90"//通用券
 * }
 * <p>
 * 帮赏分兑换通用卷
 * Created by wuxiaojun on 2017/3/16.
 */

public class GeneralExchangeVolumeResponse extends BaseResponse<GeneralExchangeVolumeResponse.BalanceExchangeBean> {

    public class BalanceExchangeBean {

        public String member_points; // 帮赏分
        public String discount_level; // 优惠百分比
        public String general_voucher; // 通用券

    }

}
