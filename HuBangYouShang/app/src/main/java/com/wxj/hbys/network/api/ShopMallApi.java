package com.wxj.hbys.network.api;

import com.wxj.hbys.bean.Response.ShopMallMainResponse;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wuxiaojun on 2017/3/7.
 */

public interface ShopMallApi {

    // 获取商城首页的数据
    @GET("mobile/index.php?act=shop&op=index")
    Observable<ShopMallMainResponse> getShopMallMainResponse();

}
