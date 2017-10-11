package com.help.reward.bean.Response;

import com.help.reward.bean.GroupGrantHelpPointsBean;

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
