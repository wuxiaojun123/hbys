package com.help.reward.rxbus.event.type;

/**
 * Created by wuxiaojun on 17-7-17.
 */

public class HomepageMemInfoRxbusType {

//    public String member_id;
    public String member_name;
    public String help_people;
    public String member_avatar;
    public String complaint;
    public String complained;
    public String description;

    public HomepageMemInfoRxbusType(String member_avatar,String member_name,String description,
                                    String help_people,String complaint,String complained){

        this.member_avatar = member_avatar;
        this.member_name = member_name;
        this.description = description;
        this.help_people = help_people;
        this.complaint = complaint;
        this.complained = complained;

    }



}
