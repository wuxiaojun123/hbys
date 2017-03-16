package com.help.reward.bean.Response;

import com.help.reward.bean.HelpRewardBean;
import com.help.reward.bean.MyBalanceBean;

import java.util.List;

/**
 * 我的账户--余额明细
 * Created by wuxiaojun on 17-3-15.
 */

public class MyBalanceResponse extends BaseResponse<MyBalanceResponse.MyBalanceData> {

    public boolean hasmore;
    public int page_total;

    public class MyBalanceData{
        public List<MyBalanceBean> list;
        public String total_num;
    }

}
