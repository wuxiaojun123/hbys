package com.help.reward.bean.Response;

import com.help.reward.bean.AreaBean;

import java.util.List;

/**
 * Created by wuxiaojun on 17-3-24.
 */

public class AeraResponse extends BaseResponse<AeraResponse.AeraData> {

    public class AeraData{
        public List<AreaBean> area_list;
    }

}
