package com.reward.help.merchant.bean;

import java.io.Serializable;

public class CouponListBean implements Serializable {

    //优惠券模板id
    public String voucher_t_id;
    //优惠券标题
    public String voucher_t_title;
    //开始结束时间
    public String voucher_t_start_date;
    public String voucher_t_end_date;
    //满
    public String voucher_t_limit;
    //减/面额
    public String voucher_t_price;
    //店铺id
    public String voucher_t_store_id;
    //店铺id/名字
    public String voucher_t_storename;
    //总数
    public String voucher_t_total;
    //已给出
    public String voucher_t_giveout;
    //优惠券图片
    public String voucher_t_customimg;

    public String voucher_t_sc_name;

    public String voucher_t_mgradelimittext;

    private boolean isSelect;

    public String getVoucher_t_id() {
        return voucher_t_id;
    }

    public void setVoucher_t_id(String voucher_t_id) {
        this.voucher_t_id = voucher_t_id;
    }

    public String getVoucher_t_title() {
        return voucher_t_title;
    }

    public void setVoucher_t_title(String voucher_t_title) {
        this.voucher_t_title = voucher_t_title;
    }

    public String getVoucher_t_start_date() {
        return voucher_t_start_date;
    }

    public void setVoucher_t_start_date(String voucher_t_start_date) {
        this.voucher_t_start_date = voucher_t_start_date;
    }

    public String getVoucher_t_end_date() {
        return voucher_t_end_date;
    }

    public void setVoucher_t_end_date(String voucher_t_end_date) {
        this.voucher_t_end_date = voucher_t_end_date;
    }

    public String getVoucher_t_limit() {
        return voucher_t_limit;
    }

    public void setVoucher_t_limit(String voucher_t_limit) {
        this.voucher_t_limit = voucher_t_limit;
    }

    public String getVoucher_t_price() {
        return voucher_t_price;
    }

    public void setVoucher_t_price(String voucher_t_price) {
        this.voucher_t_price = voucher_t_price;
    }

    public String getVoucher_t_total() {
        return voucher_t_total;
    }

    public void setVoucher_t_total(String voucher_t_total) {
        this.voucher_t_total = voucher_t_total;
    }

    public String getVoucher_t_giveout() {
        return voucher_t_giveout;
    }

    public void setVoucher_t_giveout(String voucher_t_giveout) {
        this.voucher_t_giveout = voucher_t_giveout;
    }

    public String getVoucher_t_customimg() {
        return voucher_t_customimg;
    }

    public void setVoucher_t_customimg(String voucher_t_customimg) {
        this.voucher_t_customimg = voucher_t_customimg;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getVoucher_t_store_id() {
        return voucher_t_store_id;
    }

    public void setVoucher_t_store_id(String voucher_t_store_id) {
        this.voucher_t_store_id = voucher_t_store_id;
    }

    public String getVoucher_t_storename() {
        return voucher_t_storename;
    }

    public void setVoucher_t_storename(String voucher_t_storename) {
        this.voucher_t_storename = voucher_t_storename;
    }

    public String getVoucher_t_sc_name() {
        return voucher_t_sc_name;
    }

    public void setVoucher_t_sc_name(String voucher_t_sc_name) {
        this.voucher_t_sc_name = voucher_t_sc_name;
    }

    public String getVoucher_t_mgradelimittext() {
        return voucher_t_mgradelimittext;
    }

    public void setVoucher_t_mgradelimittext(String voucher_t_mgradelimittext) {
        this.voucher_t_mgradelimittext = voucher_t_mgradelimittext;
    }
}