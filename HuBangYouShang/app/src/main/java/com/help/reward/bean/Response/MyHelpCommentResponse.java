package com.help.reward.bean.Response;


import java.util.List;

/**
 * {
 * "code": 200,
 * "msg": "操作成功",
 * "hasmore": true,
 "page_total": 2,
 * "data": [
 * {
 * "id": "1",
 * "u_id": "1",
 * "u_name": "求助者No.1",
 * "board_id": "1",
 * "board_name": "创新创业",
 * "title": "一般的电子商务网站都注册什么样的公司进行运营",
 * "create_time": "1388476693",
 * "comment": "0",
 * "offer": "1",
 * "status": "结贴",
 * "isSolved": true
 * }
 * ]
 * }
 * <p>
 * <p>
 * Created by wuxiaojun on 2017/3/1.
 */

public class MyHelpCommentResponse extends BaseResponse<List<MyHelpCommentResponse.MyHelpCommentBean>> {

    public boolean hasmore;
    public int page_total;

    public class MyHelpCommentBean{
        public String id;
        public String u_id;
        public String u_name;
        public String board_id;
        public String board_name;
        public String title;
        public String create_time;
        public String comment;
        public String offer;
        public String status;
        public boolean isSolved;
    }



}
