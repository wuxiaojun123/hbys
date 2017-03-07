package com.wxj.hbys.network;

import com.wxj.hbys.bean.Response.ShopMallMainResponse;
import com.wxj.hbys.network.api.LoginApi;
import com.wxj.hbys.network.api.ShopMallApi;
import com.wxj.hbys.network.base.RetrofitUtils;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public class ShopMallNetwork {

    // 登录api
    public static ShopMallApi getShopMallMainApi() {
        return RetrofitUtils.getRetrofit().create(ShopMallApi.class);
    }

}
