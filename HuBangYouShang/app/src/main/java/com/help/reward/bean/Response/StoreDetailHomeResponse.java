package com.help.reward.bean.Response;

import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.bean.StotrDetailBean;

import java.util.List;

/**
 * 商户详情首页
 */

public class StoreDetailHomeResponse extends BaseResponse<StoreDetailHomeResponse> {

    public StotrDetailBean store_info;
    public List<ShopMallHotBean> rec_goods_list;
    public List<ShopMallHotBean> sale_goods_list;

}
