package com.reward.help.merchant.bean;

import java.util.List;

public class PointsLogBean {

    public List<Member> receive_list;

    public VoucherInfo give_info;

    /**
     * "give_info": {
     "id": "2",
     "seller_id": "2",
     "seller_name": "liyanhong",
     "total_num": "0",
     "people_limit": "1",
     "num_limit": "0",
     "people_received": "0",
     "num_given": "0",
     "created": "1490175371",
     "member_avatar": "http://210.72.13.135/data/upload/shop/avatar/avatar_2.jpg"
     */
    public class VoucherInfo{
        public String id;
        public String seller_id;
        public String seller_name;
        public String total_num;
        public String people_limit;
        public String num_limit;
        public String people_received;
        public String num_given;
        public String created;
        public String member_avatar;
    }

    public class Member{
        public String id;
        public String member_id;
        public String member_name;
        public String created;
        public String member_avatar;
        public String num;
        public String give_log_id;
    }
}