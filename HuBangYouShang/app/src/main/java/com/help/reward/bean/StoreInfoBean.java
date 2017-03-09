package com.help.reward.bean;

/**
 * "store_info": {
 * "store_id": "1",
 * "store_name": "帮帮忙",
 * "member_id": "1",
 * "member_name": "mahuateng",
 * "is_own_shop": "1",
 * "goods_count": "8",
 * "store_huodaofk": "0",
 * "store_quick_refund": "0",
 * "store_xiaoxie": "0",
 * "store_zhping": "0",
 * "store_qtian": "1",
 * "store_credit": {
 * "store_desccredit": {
 * "text": "描述",
 * "credit": 5,
 * "percent": "----",
 * "percent_class": "equal",
 * "percent_text": "平"
 * },
 * "store_servicecredit": {
 * "text": "服务",
 * "credit": 5,
 * "percent": "----",
 * "percent_class": "equal",
 * "percent_text": "平"
 * },
 * "store_deliverycredit": {
 * "text": "物流",
 * "credit": 5,
 * "percent": "----",
 * "percent_class": "equal",
 * "percent_text": "平"
 * }
 * }
 * }
 * Created by wuxiaojun on 2017/3/8.
 */

public class StoreInfoBean {

    public String store_id;
    public String store_name;
    public StoreCredit store_desccredit; // 描述
    public StoreCredit store_servicecredit; // 服务
    public StoreCredit store_deliverycredit; // 物流

    public class StoreCredit{
        public String text;
        public String credit; // 信用，分数
        public String percent;
        public String percent_class;
        public String percent_text;
    }

}
