package com.help.reward.bean.Response;

import com.help.reward.bean.AdInfoBean;

/**
 * <p>
 *     会员看广告
 * Created by wuxiaojun on 2017/3/21.
 */

public class AdInfoResponse extends BaseResponse<AdInfoResponse.AdInfoData> {

    public class AdInfoData {
        public AdInfoBean info;
        public boolean hasWathced; // 是否观看过，ture为已看过
        public boolean isInGroup; // 是否在商家群里
        public String avaliable_groupid; // 商家群id

    }

}
