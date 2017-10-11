package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardCommentBean;

import java.util.List;

/**
 *
 */

public class HelpRewardCommentResponse extends BaseResponse<HelpRewardCommentResponse.HelpRewardCommentBeans> {

    public boolean hasmore;
    public int page_total;

    public class HelpRewardCommentBeans {
        public List<HelpRewardCommentBean> message_list;
    }

}
