package com.help.reward.bean.Response;

import com.help.reward.bean.HelpSeekCommentBean;

import java.util.List;

/**
 *求助帖子评论展开
 */

public class HelpSeekCommentDetailResponse extends BaseResponse<List<HelpSeekCommentBean>> {

    public boolean hasmore;
    public int page_total;

}
