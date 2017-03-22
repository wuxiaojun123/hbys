package com.help.reward.bean.Response;

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
    public String appeal;
    public String appeal_time;

    public String content;
    public String create_time;
    public String status;



}
