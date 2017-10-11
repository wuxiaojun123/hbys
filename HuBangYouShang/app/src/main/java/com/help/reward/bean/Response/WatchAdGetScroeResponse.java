package com.help.reward.bean.Response;

import com.help.reward.bean.AdInfoBean;

/**
 * "data": {
 * "get": "1"
 * }
 * 看完广告得分，会员看广告
 * <p>
 * Created by wuxiaojun on 2017/3/21.
 */

public class WatchAdGetScroeResponse extends BaseResponse<WatchAdGetScroeResponse.WatchAdGeScroeData> {

    public class WatchAdGeScroeData {
        public String get; // 获取到的分数
        public String rest; // 剩余分数
    }

}
