package com.help.reward.bean.Response;

import com.help.reward.bean.CouponRecordBean;

import java.util.List;

/**
 * Created by ADBrian on 03/06/2017.
 * {
 "code": 200,
 "msg": "操作成功",
 "data": [
 {
 "id": "2",
 "member_id": "6",
 "member_name": "zhouying",
 "receive_tpl_id": "4",
 "give_log_id": "17",
 "created": "1490514086",
 "give_info": {
 "id": "17",
 "seller_id": "2",
 "seller_name": "liyanhong",
 "store_id": "2",
 "voucher_tpl_id": "4",
 "num": "1",
 "num_given": "1",
 "created": "1490512541"
 },
 "tpl_info": {
 "voucher_t_price": "10",
 "voucher_t_limit": "100.00"
 }
 }
 ]
 }
 */

public class CouponsRecordResponse extends BaseResponse<List<CouponRecordBean>> {

}
