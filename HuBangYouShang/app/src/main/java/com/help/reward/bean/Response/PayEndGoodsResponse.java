package com.help.reward.bean.Response;

import com.help.reward.bean.ShopMallHotBean;

import java.util.List;

/**
 * 支付成功商品列表
 */

public class PayEndGoodsResponse extends BaseResponse<PayEndGoodsResponse> {

    public List<ShopMallHotBean> list;

}
