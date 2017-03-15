package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardBean;

import java.util.List;

/**
 * 我的账户--帮赏分明细
 * Created by wuxiaojun on 17-3-15.
 */

public class HelpRewardResponse extends BaseResponse<HelpRewardResponse.HelpRewardData> {

    public boolean hasmore;
    public int page_total;

    public class HelpRewardData{
        public List<HelpRewardBean> list;
        public String points;
    }

}
