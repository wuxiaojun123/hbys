package com.help.reward.bean;

/**
 * "store_id": "2",
 * "sc_id": "1",
 * "store_name": "我知道",
 * "store_avatar": "05451392581569638_sm.jpg",
 * "store_credit": "0",
 * "store_desccredit": "0",
 * "store_servicecredit": "0",
 * "store_deliverycredit": "0",
 * "store_collect": "0",
 * "store_avatar_url": "http://jyb.youdoidodo.com/data/upload/shop/store/05451392581569638_sm.jpg",
 * "store_evaluate_info": {
 * "store_credit": {
 * "store_desccredit": {
 * "text": "描述相符",
 * "credit": 5
 * },
 * "store_servicecredit": {
 * "text": "服务态度",
 * "credit": 5
 * },
 * "store_deliverycredit": {
 * "text": "发货速度",
 * "credit": 5
 * }
 * },
 * "store_credit_average": 5,
 * "store_credit_percent": 100
 * }
 * Created by wuxiaojun on 2017/3/8.
 */

public class SearchStoreInfoBean {

    public String store_id;
    public String store_name;
    public String store_collect;
    public String store_avatar_url;

    public StoreEvaluateInfo store_evaluate_info;

    public class StoreEvaluateInfo {
        public Storecre store_credit;
    }

    public class Storecre {
        public StoreCredit store_desccredit; // 描述
        public StoreCredit store_servicecredit; // 服务
        public StoreCredit store_deliverycredit; // 物流
    }

    public class StoreCredit {
        public String text;
        public String credit; // 信用，分数
    }

}
