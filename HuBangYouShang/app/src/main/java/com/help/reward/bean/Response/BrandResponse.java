package com.help.reward.bean.Response;

import com.help.reward.bean.BrandBean;

import java.util.List;

/**
 *
 */
public class BrandResponse extends BaseResponse<BrandResponse.BrandBeans> {

    public class BrandBeans {

        public List<BrandBean> brand_list;

    }


}
