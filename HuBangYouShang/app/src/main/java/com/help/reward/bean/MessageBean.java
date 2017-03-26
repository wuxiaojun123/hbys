package com.help.reward.bean;

/**
 * Created by admin on 2017/3/12.
 * type 0为私信、1为系统消息、2为留言、3帖子动态、4账户消息、5交易信息、
 */

public class MessageBean {
    public String message_id;
    public String message_title;
    public String message_body;
    public long message_time;
    public String related_id;
    public String message_open;
    public String message_type;
    public Remark noteObject;
    public String noteString;//help,admiration
    public class Remark{
        public String goods_image;
        public String goods_name;
        public String order_sn;
        public String store_id;
    }
}
