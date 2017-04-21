package com.help.reward.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADBrian on 15/04/2017.
 */

public class PropertyValueBean implements Serializable {
    private int property_parent_id;
    private String property_parent_name;
    private List<GoodsInfoBean.PropertyValueInfo> propertyChildList;

    public int getProperty_parent_id() {
        return property_parent_id;
    }

    public void setProperty_parent_id(int property_parent_id) {
        this.property_parent_id = property_parent_id;
    }

    public String getProperty_parent_name() {
        return property_parent_name;
    }

    public void setProperty_parent_name(String property_parent_name) {
        this.property_parent_name = property_parent_name;
    }

    public List<GoodsInfoBean.PropertyValueInfo> getPropertyChildList() {
        return propertyChildList;
    }

    public void setPropertyChildList(List<GoodsInfoBean.PropertyValueInfo> propertyChildList) {
        this.propertyChildList = propertyChildList;
    }
}
