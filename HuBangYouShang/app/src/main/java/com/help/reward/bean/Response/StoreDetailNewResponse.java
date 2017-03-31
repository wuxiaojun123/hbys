package com.help.reward.bean.Response;

import com.help.reward.bean.ShopMallHotBean;

import java.util.List;

/**
 * 商户详情上新
 */

public class StoreDetailNewResponse extends BaseResponse<StoreDetailNewResponse> {
    public boolean hasmore;
    public int page_total;
    public List<ShopMallHotBean> goods_list;

}
