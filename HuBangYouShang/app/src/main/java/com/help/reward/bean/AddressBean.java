package com.help.reward.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {
 * "address_id": "26",
 * "member_id": "2",
 * "true_name": "周绪刚",
 * "area_id": "1",
 * "city_id": "0",
 * "area_info": "东城区",
 * "address": "北京市东城区东直门南大街6号中纺大厦8层",
 * "tel_phone": "010-88703622",
 * "mob_phone": "18813015385",
 * "is_default": "0",
 * "dlyp_id": "0"
 * }
 * Created by wuxiaojun on 2017/3/20.
 */

public class AddressBean implements Parcelable {

    public String address_id;
    public String member_id;
    public String true_name;
    public String area_id;
    public String city_id;
    public String area_info; // 省份城市区域
    public String address; // 详细地址
    public String tel_phone;
    public String mob_phone;
    public String is_default;
    public String dlyp_id;
    public String pro_id;

    protected AddressBean(Parcel in) {
        address_id = in.readString();
        member_id = in.readString();
        true_name = in.readString();
        area_id = in.readString();
        city_id = in.readString();
        area_info = in.readString();
        address = in.readString();
        tel_phone = in.readString();
        mob_phone = in.readString();
        is_default = in.readString();
        dlyp_id = in.readString();
        pro_id = in.readString();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel in) {
            return new AddressBean(in);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address_id);
        dest.writeString(member_id);
        dest.writeString(true_name);
        dest.writeString(area_id);
        dest.writeString(city_id);
        dest.writeString(area_info);
        dest.writeString(address);
        dest.writeString(tel_phone);
        dest.writeString(mob_phone);
        dest.writeString(is_default);
        dest.writeString(dlyp_id);
        dest.writeString(pro_id);
    }
}
