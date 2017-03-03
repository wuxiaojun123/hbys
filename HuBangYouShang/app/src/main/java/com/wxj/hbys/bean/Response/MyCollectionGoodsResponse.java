package com.wxj.hbys.bean.Response;

import com.wxj.hbys.bean.MyCollectionGoodsBean;
import com.wxj.hbys.bean.MyCollectionPostBean;

import java.util.List;

/**
 * Created by wuxiaojun on 17-3-3.
 */

public class MyCollectionGoodsResponse extends BaseResponse<MyCollectionGoodsResponse.MyCollectionGoods> {

    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class MyCollectionGoods{

        public List<MyCollectionGoodsBean> favorites_list;

    }





}
