package com.wxj.hbys.bean.Response;

import com.wxj.hbys.bean.AdvertisementBean;
import com.wxj.hbys.fragment.BaseFragment;

import java.util.List;

/**
 *
 * Created by wuxiaojun on 17-3-2.
 */

public class AdvertisementResponse extends BaseResponse<AdvertisementResponse.Advertisement> {

    public boolean hasmore; // 是否还有更多
    public int page_total; // 总页数


    public class Advertisement{

        public List<AdvertisementBean> adv_list;

    }


}
