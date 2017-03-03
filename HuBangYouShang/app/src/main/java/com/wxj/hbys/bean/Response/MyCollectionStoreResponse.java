package com.wxj.hbys.bean.Response;

import com.wxj.hbys.bean.MyCollectionGoodsBean;
import com.wxj.hbys.bean.MyCollectionStoreBean;

import java.util.List;

/**
 * Created by wuxiaojun on 17-3-3.
 */

public class MyCollectionStoreResponse extends BaseResponse<MyCollectionStoreResponse.MyCollectionStore> {

    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class MyCollectionStore{

        public List<MyCollectionStoreBean> favorites_list;

    }





}
