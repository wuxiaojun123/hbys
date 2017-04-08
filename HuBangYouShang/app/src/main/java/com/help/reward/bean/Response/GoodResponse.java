package com.help.reward.bean.Response;

import com.help.reward.bean.GoodsInfoBean;
import com.help.reward.bean.GoodsInfoHairBean;
import com.help.reward.bean.ShopInfoRecommendBean;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.bean.StoreInfoBean;

import java.util.List;

/**
 * Created by wuxiaojun on 2017/3/8.
 */

public class GoodResponse extends BaseResponse<GoodResponse> {

    public GoodsInfoBean goods_info; // 商品信息
    public GoodsInfoHairBean goods_hair_info; // 地区
    public List<ShopMallHotBean> goods_commend_list; // 推荐商品
    public String goods_image; // 展示图片
    public StoreInfoBean store_info; // 店铺信息


}
