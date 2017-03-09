package com.help.reward.bean.Response;

import com.help.reward.bean.MyCollectionGoodsBean;

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
