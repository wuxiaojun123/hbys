package com.wxj.hbys.bean.Response;

import java.util.List;

/**
 * "code": 200,
 * "msg": "操作成功",
 * "data": [
 * {
 * "id": "5",
 * "board_id": "1",
 * "board_name": "创新创业",
 * "title": "可能会哈哈哈哈哈",
 * "create_time": "1485162050",
 * "comment": "0",
 * "admiration": "0",
 * "status": "正常"
 * }
 * <p>
 * Created by wuxiaojun on 2017/3/2.
 */

public class MyRewardPostResponse extends BaseResponse<List<MyRewardPostResponse>>{

    public String id;
    public String board_id;
    public String board_name;
    public String title;
    public String create_time;
    public String comment;
    public String admiration;
    public String status;

}
