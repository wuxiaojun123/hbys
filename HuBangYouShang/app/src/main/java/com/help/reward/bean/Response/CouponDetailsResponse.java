package com.help.reward.bean.Response;

/**
 * Created by wuxiaojun on 2017/4/30.
 */

public class CouponDetailsResponse extends BaseResponse<CouponDetailsResponse> {

    public String identity; // identity：’owner’(下架/确认交易)/’buyer’(我要交易)
    public StoreInfo store_info; // 店铺信息
    public VoucherInfo voucher_info;

    public class VoucherInfo{
        public String voucher_id;
        public String voucher_start_date; // 有效期开始时间
        public String voucher_end_date; // 有效期结束时间
        public String voucher_price; // 满多少减去额度
        public String voucher_limit; // 额度
        public String voucher_owner_setting; // 交易价格

        public String voucher_state; // 交易状态  1 未使用 2 已使用 3 已过期 4 收回 5 交易中
        // owner + 未使用=确认交易    owner+交易中 = 下架
    }

    public class StoreInfo {
        public String store_name; // 商家名称
        public String store_zy; // 主要经营
        public String area_info; // 所在地省份城市区域信息
        public String store_address; // 所在地详细信息

        public String store_desccredit; // 描述
        public String store_servicecredit; // 服务
        public String store_deliverycredit;// 物流
        public String storepraise_rate; // 好评率
        public String store_complaint;//投诉数
        public String store_phone;
        public String store_avatar; // 店铺图片
    }

}
