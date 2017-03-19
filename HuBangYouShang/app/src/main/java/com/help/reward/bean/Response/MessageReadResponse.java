package com.help.reward.bean.Response;

/**
 * /mobile/index.php?act=member_message&op=new_msg_num
 * "data": {
 * "newsystem": "1",//系统消息
 * "newpost": "2",帖子动态
 * "account": "1",账户信息
 * "trade": "0",交易消息
 * "complain": "0"投诉消息
 * }
 **/

public class MessageReadResponse extends BaseResponse<MessageReadResponse> {
    public int newsystem;
    public int newpost;
    public int account;
    public int trade;
    public int complain;

}
