package com.help.reward.bean;

/**
 * Created by admin on 2017/3/12.
 * "post_title": "想把我唱给你听，趁现在年少如花",//投诉相关帖子标题
 * "post_id": "1",//帖子id
 * "post_board": "相爱相杀",//板块
 * "update_time": "1481531799",//更新时间
 * "complainant_name": "投诉者No.1",
 * "complainant_vote": "0",//投诉者获得票数
 * "respondent_name": "被投诉者No.2",
 * "respondent_vote": "0"//被投诉者获得票数
 */

public class HelpVoteBean {
    public String post_id;
    public String post_title;
    public String post_board;
    public long update_time;
    public String complainant_name;
    public String complainant_vote;
    public String respondent_name;
    public String respondent_vote;

}
