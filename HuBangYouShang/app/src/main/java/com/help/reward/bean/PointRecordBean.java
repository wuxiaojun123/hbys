package com.help.reward.bean;

import java.util.List;

/**
 * Created by ADBrian on 03/06/2017.
 */

public class PointRecordBean {

    public String num;
    public String created;
    public PointsGiveInfo give_info;

    public class PointsGiveInfo{
        public String total_num;
        public String people_limit;
        public String number_limit;
        public String people_received;
        public String num_given;
        public String created;
    }
}
