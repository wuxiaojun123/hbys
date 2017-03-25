package com.reward.help.merchant.bean.Response;

/**
 * Created by fanjunqing on 25/03/2017.
 */

public class StoreInfoResponse extends BaseResponse<StoreInfoResponse.StoreInfoBean>{

    public class StoreInfoBean{
        public String store_name;
        public String sc_name;
        public String[] store_class_names;
        public String joinin_type;
        public String company_name;
        public String contacts_name;
        public String joinin_state;
    }
}
