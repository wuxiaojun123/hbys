package com.wxj.hbys.bean.Response;

import com.wxj.hbys.bean.AdvertisementBean;
import com.wxj.hbys.bean.MyCollectionPostBean;

import java.util.List;

/**
 * Created by wuxiaojun on 17-3-3.
 */

public class MyCollectionPostResponse extends BaseResponse<MyCollectionPostResponse.MyCollectionPost> {

    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数

    public class MyCollectionPost {

        public List<MyCollectionPostBean> favorites_list;

    }

}
