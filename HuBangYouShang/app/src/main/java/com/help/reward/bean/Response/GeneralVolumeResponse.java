package com.help.reward.bean.Response;

import com.help.reward.bean.GeneralVolumeBean;
import com.help.reward.bean.HelpRewardBean;

import java.util.List;

/**
 * 我的账户--通用卷
 * Created by wuxiaojun on 17-3-15.
 */

public class GeneralVolumeResponse extends BaseResponse<GeneralVolumeResponse.GeneralVolumData> {

    public boolean hasmore;
    public int page_total;

    public class GeneralVolumData{
        public List<GeneralVolumeBean> list;
        public String general_voucher;
    }

}
