package com.help.reward.bean.Response;

import com.help.reward.bean.AddressBean;
import com.help.reward.bean.GoodsSpecBean;
import com.help.reward.bean.VoucherBean;

import java.util.List;
import java.util.Map;

/**
 * Created by ADBrian on 07/04/2017.
 * <p>
 * { "code": 200, "msg": "操作成功", "data": { "store_cart_list": [ { "goods_list":
 * [ { "cart_id": "66", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "262", "goods_name": "我来测试", "goods_price": "6.00", "goods_num":
 * "1", "goods_image": "1_04418211827276143.jpg", "bl_id": "0", "state": true,
 * "storage_state": true, "goods_commonid": "100093", "gc_id": "186",
 * "transport_id": "0", "goods_freight": "0.00", "goods_vat": "0",
 * "goods_storage": "4", "goods_storage_alarm": "2", "is_fcode": "0",
 * "have_gift": "0", "groupbuy_info": null, "xianshi_info": null, "goods_total":
 * "6.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_04418211827276143_240.jpg"
 * }, { "cart_id": "67", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "266", "goods_name": "新鲜美味的食材", "goods_price": "30.00",
 * "goods_num": "1", "goods_image": "1_05527386478761241.png", "bl_id": "0",
 * "state": true, "storage_state": true, "goods_commonid": "100097", "gc_id":
 * "661", "transport_id": "0", "goods_freight": "10.00", "goods_vat": "0",
 * "goods_storage": "4", "goods_storage_alarm": "5", "is_fcode": "0",
 * "have_gift": "0", "groupbuy_info": null, "xianshi_info": null, "goods_total":
 * "30.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05527386478761241_240.png"
 * }, { "cart_id": "68", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "235", "goods_name": "大话西游之降魔传", "goods_price": "40.00",
 * "goods_num": "1", "goods_image": "1_04423412434387147.png", "bl_id": "0",
 * "state": true, "storage_state": true, "goods_commonid": "100089", "gc_id":
 * "1047", "transport_id": "2", "goods_freight": "0.00", "goods_vat": "0",
 * "goods_storage": "997", "goods_storage_alarm": "0", "is_fcode": "0",
 * "have_gift": "0", "groupbuy_info": null, "xianshi_info": null, "goods_total":
 * "40.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_04423412434387147_240.png"
 * } ], "store_goods_total": "76.00", "store_voucher_info": [],
 * "store_voucher_list": [], "store_name": "帮帮忙", "store_general_voucher": "10",
 * "store_id": 1 } ], "available_general_voucher": "24.90", "discount_level":
 * "12", "freight_hash":
 * "RbYCYJTnPcOnH62hZLFBcAv3UnGgOsOCja2Cec1T8frn7Lbm-TvVtb4-yPDpK1jpOGKAZvY-xOnmq_pPgfETbV-Wb89mva1HqWPBeOkaau8iNHgUJcuCblrMdV5iMG0HWPoVchvIPx4hMFzbJgcZM6u",
 * "address_info": { "address_id": "38", "member_id": "6", "true_name": "蜗居",
 * "area_id": "36", "city_id": "43", "area_info": "北京北京市石景山区", "address":
 * "出口投资者", "tel_phone": null, "mob_phone": "15885255698", "is_default": "1",
 * "dlyp_id": "0", "pro_id": "1" }, "vat_hash":
 * "s_taAEydrwXKmunbIAVFS6HJrb4E6CKIzWe", "inv_info": { "content": "不需要发票" },
 * "available_predeposit": "378.05", "available_rc_balance": null,
 * "order_amount": "76.00", "address_api": { "state": "success", "content": {
 * "1": "0.00" }, "allow_offpay": "1", "allow_offpay_batch": { "1": true },
 * "offpay_hash": "eifFZkiuaTmYudT-rmJwCkue_2JW0byS1DkjFqE",
 * "offpay_hash_batch": "VTXWAPFSjDQWe4C59LYP39_a3r2OIAwXHRZMpG4a6NiRCED" },
 * "store_final_total_list": { "1": "76.00" } } }
 */

public class ConfirmOrderResponse extends BaseResponse<ConfirmOrderResponse.ConfirmOrderBean> {

	public class ConfirmOrderBean {

		public List<ConfirmCartList>	store_cart_list;

		public String					vat_hash;

		public String					available_predeposit;

		public String					available_rc_balance;

		public String					order_amount;

		public String					general_voucher_allocation;		// 通用卷点击使用，那么将这个数据回传给后台

		public String					available_general_voucher;		// 兑换之后的通用卷

		public String					general_voucher_total_cheap;	// 通用劵能兑换的总金额

		public String					discount_level;

		public String					freight_hash;

		public InvInfoBean				inv_info;

		public AddressBean				address_info;

		public AddressApi				address_api;

	}

	public class ConfirmCartList {

		public String				store_goods_total;

		public String				store_name;

		public String				store_general_voucher;

		public String				store_id;

		public VoucherBean			store_voucher_info;		// 默认使用的优惠卷

		public List<VoucherBean>	store_voucher_list;		// 店铺全部可用优惠卷

		public List<VoucherBean>	store_voucher_list2;	// 店铺全部不可用优惠卷

		public List<GoodInfo>		goods_list;
	}

	public class GoodInfo {

		public String				goods_num;

		public String				goods_id;

		public String				goods_commonid;

		public String				goods_name;

		public String				goods_price;

		public String				goods_image_url;

		public String				cart_id;

		public String				store_id;

		public List<GoodsSpecBean>	goods_spec;		// 商品属性
	}

	/**
	 * { "address_id": "28", "member_id": "6", "true_name": "zdd", "area_id": "37",
	 * "city_id": "36", "area_info": "北京\t北京市\t东城区", "address": "asdf", "tel_phone":
	 * "010-2208333", "mob_phone": "19928982912", "is_default": "0", "dlyp_id": "0"
	 * }
	 */
	/*
	 * public class ConfirmAddressBean{ public String address_id; public String
	 * member_id; public String true_name; public String area_id; public String
	 * city_id; public String area_info; public String address; public String
	 * tel_phone; public String mob_phone; public String is_default; public String
	 * dlyp_id; public ConfirmAddressBean(){ } }
	 */

	public class InvInfoBean {

		public String content;
	}

	public class AddressApi {

		public String				offpay_hash;

		public String				offpay_hash_batch;

		public Map<String, String>	content;			// 店铺id:邮费
	}

}
/***
 * { "code": 200, "msg": "操作成功", "data": { "store_cart_list": [ { "goods_list":
 * [ { "cart_id": "66", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "262", "goods_name": "我来测试", "goods_price": "6.00", "goods_num":
 * "1", "goods_image": "1_04418211827276143.jpg", "bl_id": "0", "state": true,
 * "storage_state": true, "goods_commonid": "100093", "gc_id": "186",
 * "transport_id": "0", "goods_freight": "0.00", "goods_vat": "0",
 * "goods_storage": "4", "goods_storage_alarm": "2", "is_fcode": "0",
 * "have_gift": "0", "groupbuy_info": null, "xianshi_info": null, "goods_total":
 * "6.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_04418211827276143_240.jpg"
 * }, { "cart_id": "67", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "266", "goods_name": "新鲜美味的食材", "goods_price": "30.00",
 * "goods_num": "1", "goods_image": "1_05527386478761241.png", "bl_id": "0",
 * "state": true, "storage_state": true, "goods_commonid": "100097", "gc_id":
 * "661", "transport_id": "0", "goods_freight": "10.00", "goods_vat": "0",
 * "goods_storage": "4", "goods_storage_alarm": "5", "is_fcode": "0",
 * "have_gift": "0", "groupbuy_info": null, "xianshi_info": null, "goods_total":
 * "30.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05527386478761241_240.png"
 * }, { "cart_id": "68", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "235", "goods_name": "大话西游之降魔传", "goods_price": "40.00",
 * "goods_num": "1", "goods_image": "1_04423412434387147.png", "bl_id": "0",
 * "state": true, "storage_state": true, "goods_commonid": "100089", "gc_id":
 * "1047", "transport_id": "2", "goods_freight": "0.00", "goods_vat": "0",
 * "goods_storage": "997", "goods_storage_alarm": "0", "is_fcode": "0",
 * "have_gift": "0", "groupbuy_info": null, "xianshi_info": null, "goods_total":
 * "40.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_04423412434387147_240.png"
 * }, { "cart_id": "69", "buyer_id": "6", "store_id": "1", "store_name": "帮帮忙",
 * "goods_id": "42", "goods_name": "2015春款打底毛衫拼色毛衣 长袖套头针织衫 莺 蓝色", "goods_price":
 * "179.00", "goods_num": "1", "goods_image": "1_04418211646104580.jpg",
 * "bl_id": "0", "state": true, "storage_state": true, "goods_commonid":
 * "100009", "gc_id": "14", "transport_id": "0", "goods_freight": "0.00",
 * "goods_vat": "0", "goods_storage": "100", "goods_storage_alarm": "0",
 * "is_fcode": "0", "have_gift": "0", "groupbuy_info": null, "xianshi_info":
 * null, "goods_total": "179.00", "goods_image_url":
 * "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_04418211646104580_240.jpg"
 * } ], "store_goods_total": "255.00", "store_voucher_info": { "voucher_id":
 * "23", "voucher_code": "642440552755901006", "voucher_t_id": "11",
 * "voucher_title": "优惠券12", "voucher_desc": "满100减10", "voucher_start_date":
 * "1499411901", "voucher_end_date": "1501344000", "voucher_price": "10.00",
 * "voucher_limit": "100.00", "voucher_store_id": "1", "voucher_state": "1",
 * "voucher_active_date": "1499411901", "voucher_type": "0", "voucher_owner_id":
 * "6", "voucher_owner_name": "zhouying01", "voucher_order_id": null,
 * "voucher_from": "store", "voucher_owner_setting": "0", "desc": "面额10元 有效期至
 * 2017-07-30 消费满100.00可用" }, "store_voucher_list": { "11": { "voucher_id":
 * "23", "voucher_code": "642440552755901006", "voucher_t_id": "11",
 * "voucher_title": "优惠券12", "voucher_desc": "满100减10", "voucher_start_date":
 * "1499411901", "voucher_end_date": "1501344000", "voucher_price": "10",
 * "voucher_limit": "100.00", "voucher_store_id": "1", "voucher_state": "1",
 * "voucher_active_date": "1499411901", "voucher_type": "0", "voucher_owner_id":
 * "6", "voucher_owner_name": "zhouying01", "voucher_order_id": null,
 * "voucher_from": "store", "voucher_owner_setting": "0", "desc": "面额10元 有效期至
 * 2017-07-30 消费满100.00可用" } }, "store_name": "帮帮忙", "store_general_voucher":
 * "10", "store_id": 1 } ], "available_general_voucher": "24.90",
 * "discount_level": "12", "freight_hash":
 * "26pQl8mWyOognOUD-uwkSqNiKDKoCBOCgFvbhvqgvyaKdtOIyxRqS6PAc285YFLnBo-e7E9dCc0QdSYyCB31PzgrAbO08NI2N4x0ACC8vTbzbUTn4_QkOHfq_qeBdUB6p-L3-UR8tTNGrWBo8zL8uch",
 * "address_info": { "address_id": "38", "member_id": "6", "true_name": "蜗居",
 * "area_id": "36", "city_id": "43", "area_info": "北京北京市石景山区", "address":
 * "出口投资者", "tel_phone": null, "mob_phone": "15885255698", "is_default": "1",
 * "dlyp_id": "0", "pro_id": "1" }, "vat_hash":
 * "qbYXtsw8ojAYTW9X4aTh930xZXqBaCgvdMa", "inv_info": { "content": "不需要发票" },
 * "available_predeposit": "378.05", "available_rc_balance": null,
 * "order_amount": "10.00", "address_api": { "state": "success", "content": {
 * "1": "0.00" }, "allow_offpay": "1", "allow_offpay_batch": { "1": true },
 * "offpay_hash": "ldwUvvtQWxcDnHM7gFQRrTzETpB4wW-hBXyz_x_",
 * "offpay_hash_batch": "hmHQKkmRWCOzoQipOuabCQ4kMM1tGNCpphqvrSLTEiDQhCQ" },
 * "store_final_total_list": { "1": "10.00" } } }
 */
