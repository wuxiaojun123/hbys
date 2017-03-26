package com.help.reward.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * "area_id": "1",
 * "area_name": "北京",
 * "area_parent_id": "0"
 * Created by wuxiaojun on 17-3-24.
 */

public class AssetsAreaBean {

    public ArrayList<AreaBean> provinces;
    public ArrayList<AreaBean> cities;
    public ArrayList<AreaBean> areas;


    public class AreaBean {
        public String area_id;
        public String area_name;
        public String area_parent_id;

        public AreaBean(String area_id,String area_name,String area_parent_id){
            this.area_id = area_id;
            this.area_name = area_name;
            this.area_parent_id = area_parent_id;
        }

    }

}
