package com.help.reward.bean.Response;

/**
 * Created by wuxiaojun on 2017/4/30.
 */

public class CouponDetailsResponse extends BaseResponse<CouponDetailsResponse> {

    public String identity; // identity：’owner’(下架/确认交易)/’buyer’(我要交易)
    public StoreInfo store_info; // 店铺信息

    public class StoreInfo{
        public String store_name; // 商家名称
        public String store_zy; // 主要经营
        public String store_address; // 所在地
    }

}
