package com.wxj.hbys.bean.Response;

import com.wxj.hbys.bean.GoodsInfoBean;
import com.wxj.hbys.bean.GoodsInfoHairBean;
import com.wxj.hbys.bean.ShopInfoRecommendBean;
import com.wxj.hbys.bean.ShopMallHotBean;
import com.wxj.hbys.bean.StoreInfoBean;

import java.util.List;

/**
 * Created by wuxiaojun on 2017/3/8.
 */

public class GoodResponse extends BaseResponse {

    public GoodsInfoBean goods_info; // 商品信息
    public GoodsInfoHairBean goods_hair_info; // 地区
    public List<ShopInfoRecommendBean> goods_commend_list; // 推荐商品
    public String goods_image; // 展示图片
    public StoreInfoBean store_info; // 店铺信息


}
