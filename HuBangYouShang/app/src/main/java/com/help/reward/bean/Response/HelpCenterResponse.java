package com.help.reward.bean.Response;

import com.help.reward.bean.HelpCenterBean;

import java.util.List;

/**
 * {
 * "article_id": "6",
 * "article_title": "如何注册成为会员"
 * }
 * 帮助中心
 * Created by wuxiaojun on 17-3-17.
 */

public class HelpCenterResponse extends BaseResponse<HelpCenterResponse.HelpCenterData> {


    public class HelpCenterData {

        public List<HelpCenterBean> article_list;

    }

}
