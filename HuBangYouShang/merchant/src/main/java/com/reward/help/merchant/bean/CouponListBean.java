package com.reward.help.merchant.bean;

/**
 * Created by fanjunqing on 21/03/2017.
 */

public class CouponListBean {
    //优惠金额
    private String discount;
    //额度
    private String limit;
    //店铺名称
    private String storeName;
    //优惠时间
    private String date;
    //数量
    private String count;

    private boolean isChecked;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
