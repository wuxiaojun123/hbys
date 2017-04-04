package com.help.reward.bean.Response;

import java.util.List;

/**
 * Created by ADBrian on 02/04/2017.
 */

public class CartInfoBean {

    public String store_id;
    public String store_name;

    public List<GoodInfoBean> goods;

    public class GoodInfoBean{
        public String goods_id;
        public String goods_name; // 商品名称
        public String goods_price; // 商品价格
        public String goods_num; // 商品数量
        public String store_id;
        public String goods_image_url; // 图片路径
        public String cart_id; // 购物车
        public String goods_commonid; // 购物车
        public String goods_jingle; // 购物车
        public String store_name; //
        public String gc_id; //
        public String gc_id_1; //
        public String gc_id_2; //
        public String gc_id_3; //
        public String brand_id; //
        public String goods_promotion_price; //
        public String goods_promotion_type; //
        public String goods_marketprice; //
        public String goods_serial; //
        public String goods_storage_alarm; //
        public String goods_click; //
        public String goods_salenum; //
        public String goods_collect; //
        public String goods_spec; //
        public String goods_storage; //
        public String goods_image; //
        public String goods_state; //
        public String goods_verify; //
        public String goods_addtime; //
        public String goods_edittime; //
        public String areaid_1; //
        public String areaid_2; //
        public String color_id; //
        public String transport_id; //
        public String goods_freight; //
        public String goods_vat; //
        public String goods_commend; //
        public String goods_stcids; //
        public String evaluation_good_star; //
        public String evaluation_count; //
        public String is_virtual; //
        public String virtual_indate; //
        public String virtual_limit; //
        public String virtual_invalid_refund; //
        public String is_fcode; //
        public String is_appoint; //
        public String is_presell; //
        public String have_gift; //
        public String is_own_shop;
        public String goods_barcode;
        public String gift_list;
    }
}
