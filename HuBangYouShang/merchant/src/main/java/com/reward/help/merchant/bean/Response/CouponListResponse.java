package com.reward.help.merchant.bean.Response;


public class CouponListResponse extends BaseResponse {
    //优惠券模板id
    public String voucher_t_id;
    //优惠券标题
    public String voucher_t_title;
    //开始结束时间
    public String voucher_t_start_date;
    public String oucher_t_end_date;
    //满
    public String voucher_t_limit;
    //减/面额
    public String voucher_t_price;
    //店铺id/名字
    public String voucher_t_store;
    //总数
    public String voucher_t_total;
    //已给出
    public String voucher_t_giveout;
    //优惠券图片
    public String voucher_t_customimg;
}
