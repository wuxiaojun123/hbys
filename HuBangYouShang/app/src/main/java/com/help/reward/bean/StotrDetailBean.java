package com.help.reward.bean;

import java.util.List;

/**
 * Created by admin on 2017/3/29.
 * "store_id": "2",
 * "store_name": "我知道",
 * "member_id": "2",
 * "store_avatar": "http://192.168.3.2/data/upload/shop/store/05375366771834238_sm.jpg",
 * "goods_count": 3,
 * "store_collect": 0,
 * "is_favorate": false,
 * "is_own_shop": false,
 * "store_credit_text": "描述: 5.0, 服务: 5.0, 物流: 5.0",
 * "store_banner": "http://192.168.3.2/data/upload/shop/store/05375361533099808.jpg",
 * "store_slide": [
 * {
 * "img": "http://192.168.3.2/data/upload/shop/store/05375447441240354.jpg",
 * "imgUrl": "http://duomamu.com"
 * },
 * {
 * "img": "http://192.168.3.2/data/upload/shop/store/05375447538978866.jpg",
 * "imgUrl": "http://duomamu.com"
 * }
 * ]
 */

public class StotrDetailBean {

    public String store_id;
    public String store_name;
    public String store_avatar;
    public String store_collect;
    public String store_banner;
    public List<StoreSlide> store_slide;
    public boolean is_favorate;
    public boolean is_own_shop;
    public class StoreSlide {
        public String img;
        public String imgUrl;
    }

}
