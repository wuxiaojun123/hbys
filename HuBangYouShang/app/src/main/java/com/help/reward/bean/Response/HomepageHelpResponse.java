package com.help.reward.bean.Response;

import com.help.reward.bean.HomepageBean;
import com.help.reward.bean.HomepageMemberInfoBean;

import java.util.List;

/**
 * 求助
 * Created by wuxiaojun on 17-7-17.
 */

public class HomepageHelpResponse extends BaseResponse<HomepageHelpResponse.HomepageHelpBean> {

    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数

    public class HomepageHelpBean {

        public HomepageMemberInfoBean member_info;
        public List<HomepageBean> seek_help;

    }

}
