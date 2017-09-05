package com.help.reward.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class AdvertisementBean implements Parcelable{

    public String id;
    public String url;
    public String name;
    public String user_name;
    public String credit;
    public String type;
    public boolean hasWatched; // true：看过当前广告
    public String per_credit; // 单次点击得积分数
    public String groupid; // 群id

    protected AdvertisementBean(Parcel in) {
        id = in.readString();
        url = in.readString();
        name = in.readString();
        user_name = in.readString();
        credit = in.readString();
        type = in.readString();
    }

    public static final Creator<AdvertisementBean> CREATOR = new Creator<AdvertisementBean>() {
        @Override
        public AdvertisementBean createFromParcel(Parcel in) {
            return new AdvertisementBean(in);
        }

        @Override
        public AdvertisementBean[] newArray(int size) {
            return new AdvertisementBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(user_name);
        dest.writeString(credit);
        dest.writeString(type);
    }
}
