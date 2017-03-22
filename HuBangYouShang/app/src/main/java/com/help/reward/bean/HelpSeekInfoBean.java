package com.help.reward.bean;

import java.util.List;

/**
 * 求助详情
 * Created by xfz on 2017/3/20.
 */

public class HelpSeekInfoBean {
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
    public String offer;
    public String area_id;
    public long end_time;
    public List<String> img_url;
}
