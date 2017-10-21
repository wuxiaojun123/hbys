package com.reward.help.merchant.bean.Response;


import com.reward.help.merchant.bean.GroupGrantHelpPointsBean;

import java.util.List;

/**
 * Created by wuxiaojun on 17-8-9.
 */

public class GroupGrantHelpPointsResponse extends BaseResponse<GroupGrantHelpPointsResponse.GroupGrantHelpPoints> {

    public boolean hasmore;
    public int page_total;

    public class GroupGrantHelpPoints{
        public List<GroupGrantHelpPointsBean> giveList;
    }


}
