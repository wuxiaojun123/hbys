package com.help.reward.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {
 * "goods_id": "312",
 * "spec_str": "469|471",
 * "goods_name": "利朗西服 黑色 170/92",
 * "spec_image": "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05581777584782610_360.jpg",
 * "goods_price": "101.00",
 * "goods_marketprice": "101.00",
 * "goods_salenum": "0",
 * "goods_storage": "991",
 * "goods_image": "http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05581777584782610_360.jpg,http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05581777866565987_360.jpg,http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05581777804961175_360.jpg,http://www.hubangyoushang.com/data/upload/shop/store/goods/1/1_05581777752432026_360.jpg",
 * "goods_eval_list": [],
 * "goods_evaluate_info": {
 * "good": 0,
 * "normal": 0,
 * "bad": 0,
 * "all": 0,
 * "good_percent": 100,
 * "normal_percent": 0,
 * "bad_percent": 0,
 * "good_star": 5,
 * "star_average": 5
 * }
 * }
 * Created by wuxiaojun on 2017/9/10.
 */

public class GoodSpecBean implements Parcelable {
    public String goods_id;
    public String goods_name;
    public String goods_price;
    public String goods_marketprice;
    public String goods_salenum;
    public String goods_storage;
    public String goods_image;
    public String spec_image; // 商品缩略图
    public String spec_str; // 商品规格

    protected GoodSpecBean(Parcel in) {
        goods_id = in.readString();
        goods_name = in.readString();
        goods_price = in.readString();
        goods_marketprice = in.readString();
        goods_salenum = in.readString();
        goods_storage = in.readString();
        goods_image = in.readString();
        spec_image = in.readString();
        spec_str = in.readString();
    }

    public static final Creator<GoodSpecBean> CREATOR = new Creator<GoodSpecBean>() {
        @Override
        public GoodSpecBean createFromParcel(Parcel in) {
            return new GoodSpecBean(in);
        }

        @Override
        public GoodSpecBean[] newArray(int size) {
            return new GoodSpecBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goods_id);
        dest.writeString(goods_name);
        dest.writeString(goods_price);
        dest.writeString(goods_marketprice);
        dest.writeString(goods_salenum);
        dest.writeString(goods_storage);
        dest.writeString(goods_image);
        dest.writeString(spec_image);
        dest.writeString(spec_str);
    }
}
