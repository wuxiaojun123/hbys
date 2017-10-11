package com.help.reward.bean.Response;

import java.util.List;

/**
 *
 */

public class HelpVoteInfoResponse extends BaseResponse<HelpVoteInfoResponse> {

    public String has_voted;//false/vote_for_cpn/vote_for_rpd(还未投票/支持投诉者/支持被投诉者)’}
    public String complainant_name;
    public String complainant_avatar;
    public String complainant_id;
    public String complainant_vote;


    public String respondent_name;
    public String respondent_avatar;
    public String respondent_id;
    public String respondent_vote;


    public String post_title;
    public String appeal; // 申诉内容
    public String appeal_time;

    public String content; // 投诉内容
    public String create_time;
    public String status;

    public String complainant_explain; // 投诉者解释
    public String respondent_explain; // 被投诉者解释

    public List<String> content_img; // 投诉图片
    public List<String> appeal_img; // 申诉图片
    public List<String> complainant_explain_img; // 解释图片
    public List<String> respondent_explain_img; // 解释图片

}
