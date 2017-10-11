package com.help.reward.bean.Response;

import com.help.reward.bean.AddressBean;

import java.util.List;

/**
 *
 * 地址管理
 * Created by wuxiaojun on 2017/3/20.
 */

public class AddressResponse extends BaseResponse<AddressResponse.AddressData> {

    public class AddressData{
        public List<AddressBean> address_list;
    }

}
