package com.help.reward.bean.Response;

import com.help.reward.bean.HelpVoteBean;

import java.util.List;

/**
 *
 */

public class HelpVoteResponse extends BaseResponse<HelpVoteResponse.HelpVoteBeans> {
    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class HelpVoteBeans{

        public List<HelpVoteBean> post_list;

    }
}
