package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardCommentBean;

import java.util.List;

/**
 *
 */

public class HelpRewardChatResponse extends BaseResponse<HelpRewardChatResponse.HelpRewardCommentBeans> {

    public boolean hasmore;
    public int page_total;

    public class HelpRewardCommentBeans {
        public List<HelpRewardCommentBean> chat_list;
    }

}
