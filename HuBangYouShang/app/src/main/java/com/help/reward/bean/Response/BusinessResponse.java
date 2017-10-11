package com.help.reward.bean.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 行业
 * Created by wuxiaojun on 2017/3/26.
 */

public class BusinessResponse extends BaseResponse<BusinessResponse.BusinessData> {

    public class BusinessData {
        public ArrayList<String> business;
    }

}
