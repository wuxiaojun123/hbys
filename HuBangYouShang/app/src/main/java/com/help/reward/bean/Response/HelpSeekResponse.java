package com.help.reward.bean.Response;

import com.help.reward.bean.HelpSeekBean;

import java.util.List;

/**
 *
 */

public class HelpSeekResponse extends BaseResponse<HelpSeekResponse.HelpSeekBeans> {
    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class HelpSeekBeans{

        public List<HelpSeekBean> post_list;

    }
}
