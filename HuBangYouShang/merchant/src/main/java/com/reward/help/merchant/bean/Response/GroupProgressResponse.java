package com.reward.help.merchant.bean.Response;

import java.util.List;

/**
 * Created by ADBrian on 08/04/2017.
 */

public class GroupProgressResponse extends BaseResponse<GroupProgressResponse.GroupProgrossBean> {


    public class GroupProgrossBean{
        public List<GroupProgrossInfoBean> list;
    }


    public class GroupProgrossInfoBean{
        public String content;
        public String status;
        public String created;
    }
}
