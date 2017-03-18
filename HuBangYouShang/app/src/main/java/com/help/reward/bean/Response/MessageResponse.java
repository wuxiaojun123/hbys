package com.help.reward.bean.Response;

import com.help.reward.bean.MessageBean;

import java.util.List;

/**
 *
 */

public class MessageResponse extends BaseResponse<MessageResponse.MessageBeans> {
    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class MessageBeans{

        public List<MessageBean> list;

    }
}
