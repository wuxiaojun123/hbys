package com.help.reward.bean;

import java.util.List;

/**
 * 赞赏详情
 * Created by xfz on 2017/3/20.
 */

public class HelpRewardInfoBean {
    public String id;
    public String board_id;
    public String u_id;
    public String u_name;
    public String member_avatar;
    public String title;
    public String content;
    public long create_time;
    public String click_num;
    public String status;//  '正常','结贴'
    public String last_re_u_id;
    public String last_re_u_name;
    public String comment;
    public List<String> img_url;
    public String admiration;
    public String admiration_all;
    public List<AdmirationBean> admiration_list;

}
