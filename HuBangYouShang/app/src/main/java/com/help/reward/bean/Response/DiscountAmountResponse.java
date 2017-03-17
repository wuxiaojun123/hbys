package com.help.reward.bean.Response;

/**
 * 优惠金额
 * Created by wuxiaojun on 2017/3/16.
 */

public class DiscountAmountResponse extends BaseResponse<DiscountAmountResponse.DiscountAmountBean>{

    public class DiscountAmountBean{

        public String discount_level; // 优惠等级和优惠百分比是一个数
        public String rule; // 文字

    }

}
