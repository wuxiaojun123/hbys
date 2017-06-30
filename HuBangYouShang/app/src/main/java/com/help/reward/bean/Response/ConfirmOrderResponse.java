package com.help.reward.bean.Response;

import com.help.reward.bean.AddressBean;

import java.util.List;

/**
 * Created by ADBrian on 07/04/2017.
 *
 * {
 "code": 200,
 "msg": "操作成功",
 "data": {
 "store_cart_list": [
 {
 "goods_list": [
 {
 "goods_num": 1,
 "goods_id": 38,
 "goods_commonid": "100008",
 "gc_id": "14",
 "store_id": "1",
 "goods_name": "正品 2015春装新款 女 绣花针织衫 开衫外套浮桑初 蓝色",
 "goods_price": "189.00",
 "store_name": "帮帮忙",
 "goods_image": "1_04418207207476705.jpg",
 "transport_id": "0",
 "goods_freight": "0.00",
 "goods_vat": "0",
 "goods_storage": "56",
 "goods_storage_alarm": "0",
 "is_fcode": "0",
 "have_gift": "0",
 "state": true,
 "storage_state": true,
 "groupbuy_info": null,
 "xianshi_info": null,
 "cart_id": 38,
 "bl_id": 0,
 "goods_total": "189.00",
 "goods_image_url": "http://jyb.youdoidodo.com/data/upload/shop/store/goods/1/1_04418207207476705_240.jpg"
 }
 ],
 "store_goods_total": "189.00",
 "store_voucher_info": [],
 "store_voucher_list": [],
 "store_name": "帮帮忙",
 "store_general_voucher": "0",
 "store_id": 1
 }
 ],
 "available_general_voucher": "0.00",
 "discount_level": "10",
 "freight_hash": "a0zUMb2xXpyZRbTbqmUecuFcRCAEc32C3nsn_b5uYp5ZARiD8n44i20ZbEUwD4O5A2czli8hXMccZ28vymsgkb6p0Vc10I0CiInyEjHkEXmw_En5n5FjgPb",
 "address_info": {
 "address_id": "28",
 "member_id": "6",
 "true_name": "zdd",
 "area_id": "37",
 "city_id": "36",
 "area_info": "北京\t北京市\t东城区",
 "address": "asdf",
 "tel_phone": "010-2208333",
 "mob_phone": "19928982912",
 "is_default": "0",
 "dlyp_id": "0"
 },
 "vat_hash": "tECpnCI5sA2AmwayREWKnEJLNeFYVIMG3Z1",
 "inv_info": {
 "content": "不需要发票"
 },
 "available_predeposit": "428.00",
 "available_rc_balance": null,
 "order_amount": "189.00",
 "address_api": {
 "state": "success",
 "content": {
 "1": "0.00"
 },
 "allow_offpay": "1",
 "allow_offpay_batch": {
 "1": true
 },
 "offpay_hash": "AYZW-SGJ7AIO3M6yiHrm8dp3DAZhZ90Zm6YamM6",
 "offpay_hash_batch": "HcWHIfbqUPZnATD1ShBGL-hyF7TL53darRTy4BZBIk8WHH8"
 },
 "store_final_total_list": {
 "1": "189.00"
 }
 }
 }
 */

public class ConfirmOrderResponse extends BaseResponse<ConfirmOrderResponse.ConfirmOrderBean> {

    public class ConfirmOrderBean{
        public List<ConfirmCartList> store_cart_list;
        public String vat_hash;
        public String available_predeposit;
        public String available_rc_balance;
        public String order_amount;

        public String available_general_voucher;
        public String discount_level;
        public String freight_hash;

        public InvInfoBean inv_info;
        public AddressBean address_info;
        public AddressApi address_api;

    }


    public class ConfirmCartList{
        public String store_goods_total;
        public String store_name;
        public String store_general_voucher;
        public String store_id;
        //public String[] store_voucher_info;
        //public String[] store_voucher_list;

        public List<GoodInfo> goods_list;
    }


    public class GoodInfo{
        public String goods_num;
        public String goods_id;
        public String goods_commonid;
        public String goods_name;
        public String goods_price;
        public String goods_image_url;
        public String cart_id;
        public String store_id;
    }

    /**
     * {
     "address_id": "28",
     "member_id": "6",
     "true_name": "zdd",
     "area_id": "37",
     "city_id": "36",
     "area_info": "北京\t北京市\t东城区",
     "address": "asdf",
     "tel_phone": "010-2208333",
     "mob_phone": "19928982912",
     "is_default": "0",
     "dlyp_id": "0"
     }
     */
    /*public class ConfirmAddressBean{
        public String address_id;
        public String member_id;
        public String true_name;
        public String area_id;
        public String city_id;
        public String area_info;
        public String address;
        public String tel_phone;
        public String mob_phone;
        public String is_default;
        public String dlyp_id;

        public ConfirmAddressBean(){

        }
    }*/


    public class InvInfoBean{
        public String content;
    }

    public class AddressApi{
        public String offpay_hash;
        public String offpay_hash_batch;
    }
}
