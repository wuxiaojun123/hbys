package com.help.reward.bean.Response;

import java.util.List;

/**
 * "id": "2",
 * "complainant_id": "2",
 * "complainant_name": "mahuateng",
 * "complainant_vote": "0",
 * "respondent_id": "1",
 * "respondent_name": "mahuateng",
 * "respondent_vote": "0",
 * "post_id": "1",
 * "post_type": "help",
 * "post_title": "一般的电子商务网站都注册什么样的公司进行运营",
 * "post_board": "创新创业",
 * "create_time": "1487088000",
 * "content": "我疯啦，我投诉自己啦",
 * "appeal": "阳光在手指尖。",
 * "appeal_time": "1484793515",
 * "complainant_explain": null,
 * "com_explain_time": null,
 * "respondent_explain": "大家好，我是MC子龙。",
 * "respondent_time": "1484796441",
 *
 * "update_time": "1487088000",
 * "status": "和解",
 * "result": "",
 * "vote_for_cpn": ",2",
 * "vote_for_rpd": null
 * Created by wuxiaojun on 17-3-3.
 */

public class MyVoteResponse extends BaseResponse<List<MyVoteResponse>> {

    public String id;
    public String complainant_id;
    public String complainant_name;
    public String complainant_vote;
    public String respondent_id;
    public String respondent_name;
    public String respondent_vote;
    public String post_id;
    public String post_type;
    public String post_title;
    public String post_board;
    public String create_time;
    public String content;
    public String appeal;
    public String appeal_time;
    public String complainant_explain;
    public String com_explain_time;
    public String respondent_explain;
    public String respondent_time;
    public String update_time;
    public String status;
    public String result;
    public String vote_for_cpn;
    public String vote_for_rpd;


}
