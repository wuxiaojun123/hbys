package com.help.reward.bean.Response;

import java.util.List;

/**
 * Created by ADBrian on 02/04/2017.
 */

public class ShopCartResponse extends BaseResponse<ShopCartResponse.ShopCartListBean> {

    public class ShopCartListBean{
        public List<CartInfoBean> cart_list;
        public String sum;
        public int cart_count;
    }
}
