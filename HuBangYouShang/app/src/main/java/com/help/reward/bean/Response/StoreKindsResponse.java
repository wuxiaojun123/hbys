package com.help.reward.bean.Response;

import com.help.reward.bean.StoreKindsBean;

import java.util.List;

/**
 *
 */
public class StoreKindsResponse extends BaseResponse<StoreKindsResponse.StoreKindsBeans> {

    public class StoreKindsBeans {

        public List<StoreKindsBean> store_goods_class;

    }


}
