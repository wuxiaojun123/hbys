package com.help.reward.bean.Response;

import com.help.reward.bean.ShopMallHotBean;

import java.util.List;

/**
 * 商户详情全部
 */

public class StoreDetailAllResponse extends BaseResponse<StoreDetailAllResponse> {

    public boolean hasmore;
    public int page_total;
    public List<ShopMallHotBean> goods_list;

}
