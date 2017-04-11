package com.help.reward.bean.Response;

import com.help.reward.bean.SearchStoreInfoBean;

import java.util.List;

/**
 *
 */

public class SearchStoreResponse extends BaseResponse<SearchStoreResponse.SearchStoreInfoBeans> {

    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数

    public class SearchStoreInfoBeans {
        public List<SearchStoreInfoBean> list;
    }

}
