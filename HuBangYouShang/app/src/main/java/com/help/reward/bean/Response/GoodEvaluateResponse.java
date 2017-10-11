package com.help.reward.bean.Response;


import com.help.reward.bean.GoodEvaluateBean;
import com.help.reward.bean.GoodEvaluateCountBean;

import java.util.List;

public class GoodEvaluateResponse extends BaseResponse<GoodEvaluateResponse.GoodEvaluateListBean> {
    public boolean hasmore;
    public int page_total;

    public class GoodEvaluateListBean{
        public List<GoodEvaluateBean> goods_eval_list;
        public GoodEvaluateCountBean goods_eval_info;
    }

}
