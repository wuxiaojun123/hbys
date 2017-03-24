package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardInfoBean;

/**
 *
 */

public class HelpRewardInfoResponse extends BaseResponse<HelpRewardInfoResponse> {

    public String has_commented;
    public String has_chatted;//私聊过
    public String could_comment;//是否允许评论
    public HelpRewardInfoBean info;
}
