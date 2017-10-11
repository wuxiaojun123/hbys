package com.help.reward.bean.Response;

import com.help.reward.bean.HelpBoardBean;

import java.util.List;

/**
 *
 */
public class HelpBoardResponse extends BaseResponse<HelpBoardResponse.HelpBoardBeans> {

    public class HelpBoardBeans {

        public List<HelpBoardBean> board_list;

    }
}
