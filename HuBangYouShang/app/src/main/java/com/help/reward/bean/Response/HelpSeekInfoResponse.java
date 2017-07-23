package com.help.reward.bean.Response;

import com.help.reward.bean.HelpSeekCommentBean;
import com.help.reward.bean.HelpSeekInfoBean;

import java.util.List;

/**
 *
 */

public class HelpSeekInfoResponse extends BaseResponse<HelpSeekInfoResponse> {

    public String comment_num;
    public String has_commented;
    public String commented_type;
    public boolean hasmore;
    public boolean has_helped;
    public int page_total;
    public HelpSeekInfoBean info;
    public List<HelpSeekCommentBean> comment;
    public String has_collected; // 当前帖子是否收藏过  0/1 没收藏/已收藏

}
