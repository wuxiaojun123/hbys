package com.help.reward.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情
 * Created by wuxiaojun on 2017/3/8.
 */

public class GoodsInfoBean implements Serializable{

    public String goods_name; // 商品名称
    public String goods_jingle;
    public String goods_price; // 现价
    public String goods_marketprice; // 原价
    public String goods_discount; // 折扣
    public String goods_salenum;// 多少人付款
    public String goods_storage; // 库存
    public String goods_url;
    public List<PropertyName> spec_name;
    public List<PropertyValue> spec_value;

    public class PropertyName implements Serializable{
        public int spec_id;
        public String spec_name;
    }

    public class PropertyValue implements Serializable{
        public int spec_id;
        public List<PropertyValueInfo> spec_value;
    }

    public class PropertyValueInfo implements Serializable{
        public int spec_value_id;
        public String spec_value_name;
    }

}
