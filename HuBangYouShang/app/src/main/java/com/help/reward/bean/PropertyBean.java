package com.help.reward.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADBrian on 15/04/2017.
 */

public class PropertyBean implements Serializable{
    private String goods_pic; // 图片链接
    private String goods_name;
    private String goods_num;
    private String goods_price;
    private String goods_id;
    private String tip;
    public String goods_img; // 商品轮播图片
    public String goods_marketprice;
    public String goods_salenum;// 多少人已付款

    /**
     * 选择的属性
     */

    private String selectNum;


    private List<PropertyValueBean> propertyList;

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public List<PropertyValueBean> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<PropertyValueBean> propertyList) {
        this.propertyList = propertyList;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(String selectNum) {
        this.selectNum = selectNum;
    }
}
