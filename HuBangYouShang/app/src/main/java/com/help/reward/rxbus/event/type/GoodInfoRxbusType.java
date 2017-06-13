package com.help.reward.rxbus.event.type;

/**
 * Created by wuxiaojun on 17-6-2.
 */

public class GoodInfoRxbusType {

    public boolean collection; // 是否已收藏
    public boolean is_in_group; // 标记是否已经加入群组
    public String member_id; // 加商家群的时候需要用到

    public GoodInfoRxbusType(boolean collection, boolean is_in_group,
                             String member_id) {
        this.collection = collection;
        this.is_in_group = is_in_group;
        this.member_id = member_id;
    }

}
