package com.help.reward.bean.Response;

import java.util.List;

/**
 *
 */

public class HelpComplainedDetailResponse extends BaseResponse<HelpComplainedDetailResponse> {

    public String id;
    public String complainant_id;//原告、投诉人id
    public String complainant_name;//投诉人姓名
    public String complainant_vote;


    public String respondent_id;
    public String respondent_name;
    public String respondent_vote;

    public String post_id;
    public String post_title;
    public String post_type;
    public String post_board;
    public String create_time;


    public String content;//投诉理由
    public String complainant_explain;//投诉人解释
    public String com_explain_time;
    public List<String> content_img;
    public List<String> appeal_img;
    public List<String> respondent_explain_img;
    public List<String> complainant_explain_img;
    public String appeal;//申诉理由
    public String appeal_time;
    public String respondent_explain;//被投诉者解释
    public String respondent_time;


    public String update_time;
    public String status;


}
