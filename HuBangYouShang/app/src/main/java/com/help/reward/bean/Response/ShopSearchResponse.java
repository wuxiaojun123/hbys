package com.help.reward.bean.Response;

import java.util.List;

/**
 * Created by wuxiaojun on 2017/3/31.
 */

public class ShopSearchResponse extends BaseResponse<ShopSearchResponse.ShopSearchData> {

    public class ShopSearchData {
        public String[] list; // 热门搜索
        public String[] his_list; // 历史记录
    }

}
