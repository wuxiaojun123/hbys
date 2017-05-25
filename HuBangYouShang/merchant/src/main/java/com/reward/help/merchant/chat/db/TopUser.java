package com.reward.help.merchant.chat.db;

/**
 * Created by ADBrian on 08/04/2017.
 */

public class TopUser {

    protected String topuser_id;
    protected String is_group;
    protected Long time;

    public String getTopuser_id() {
        return topuser_id;
    }

    public void setTopuser_id(String topuser_id) {
        this.topuser_id = topuser_id;
    }

    public String getIs_group() {
        return is_group;
    }

    public void setIs_group(String is_group) {
        this.is_group = is_group;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
