package com.help.reward.bean.Response;

/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class WXLoginTokenResponse extends BaseResponse<WXLoginTokenResponse.WXLoginToken> {



    public class WXLoginToken {

        public String access_token;
        public String expires_in;
        public String refresh_token;
        public String openid;
        public String scope;
        public String unionid;

    }

}
