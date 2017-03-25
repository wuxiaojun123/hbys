package com.reward.help.merchant.network.api;


import com.reward.help.merchant.bean.Response.StoreInfoResponse;
import com.reward.help.merchant.utils.Constant;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface StoreApi {
    // 获取code 注册和修改密码
    @FormUrlEncoded
    @POST(Constant.URL_GET_STORE)
    Observable<StoreInfoResponse> getStoreInfo(
            @Field("key") String key
    );
}
