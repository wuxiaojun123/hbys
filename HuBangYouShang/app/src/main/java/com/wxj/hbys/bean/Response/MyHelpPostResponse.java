package com.wxj.hbys.bean.Response;

import com.wxj.hbys.bean.HelpPostBean;

import java.util.List;

/**
 *
 * Created by wuxiaojun on 2017/3/1.
 */

public class MyHelpPostResponse extends BaseResponse<List<MyHelpPostResponse>> {

    public String id;
    public String board_id;
    public String board_name;
    public String title;
    public String create_time;
    public String comment;
    public String offer;
    public String status;
    public boolean isSolved;

}
