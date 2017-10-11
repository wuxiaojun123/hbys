package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardCommentBean;

import java.util.List;

/**
 *
 */

public class HelpRewardChatDetailResponse extends BaseResponse<List<HelpRewardCommentBean>> {

    public boolean hasmore;
    public int page_total;

}
