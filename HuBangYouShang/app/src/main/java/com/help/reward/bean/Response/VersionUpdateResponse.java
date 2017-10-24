package com.help.reward.bean.Response;

/**
 * Created by wuxiaojun on 2017/10/24.
 */

public class VersionUpdateResponse extends BaseResponse<VersionUpdateResponse.VersionUpdateBean> {


    public class VersionUpdateBean{
        public String content;
        public String version;
        public String linkUrl;

    }

}
