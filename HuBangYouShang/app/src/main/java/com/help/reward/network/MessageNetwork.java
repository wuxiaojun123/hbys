package com.help.reward.network;

import com.help.reward.network.api.MessageApi;
import com.help.reward.network.base.RetrofitUtils;

/**
 *
 */

public class MessageNetwork {

    public static MessageApi getMessageApi() {
        return RetrofitUtils.getRetrofitCookie().create(MessageApi.class);
    }

}
