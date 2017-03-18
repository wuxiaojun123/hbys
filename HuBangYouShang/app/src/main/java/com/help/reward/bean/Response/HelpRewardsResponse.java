package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardsBean;

import java.util.List;

/**
 *
 */

public class HelpRewardsResponse extends BaseResponse<HelpRewardsResponse.HelpRewardsBeans> {
    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class HelpRewardsBeans{

        public List<HelpRewardsBean> post_list;

    }
}
