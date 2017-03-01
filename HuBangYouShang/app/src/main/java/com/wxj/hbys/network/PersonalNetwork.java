package com.wxj.hbys.network;

import com.wxj.hbys.network.api.PersonalApi;

/**
 * Created by wuxiaojun on 2017/3/1.
 */

public class PersonalNetwork {


    public static PersonalApi getMyHelpPostResponseApi(){
        return RetrofitUtils.getRetrofitCookie().create(PersonalApi.class);
    }


}
