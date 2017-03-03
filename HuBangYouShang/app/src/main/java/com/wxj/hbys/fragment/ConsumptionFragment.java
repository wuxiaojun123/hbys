package com.wxj.hbys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.wxj.hbys.R;
import com.wxj.hbys.activity.GoodInfoActivity;
import com.wxj.hbys.activity.ShopInfoActivity;
import com.wxj.hbys.view.MyGridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页-消费
 * Created by wuxiaojun on 2017/1/8.
 */

public class ConsumptionFragment extends BaseFragment {

    @BindView(R.id.tv_title_help_msgcount)
    TextView tvTitleHelpMsgcount;
    @BindView(R.id.layout_help_title_sms)
    LinearLayout layoutHelpTitleSms;
    @BindView(R.id.et_shop_search)
    EditText etShopSearch;
    @BindView(R.id.banner_shop)
    ConvenientBanner bannerShop;
    @BindView(R.id.layout_shop_myaccount)
    LinearLayout layoutShopMyaccount;
    @BindView(R.id.layout_shop_myorder)
    LinearLayout layoutShopMyorder;
    @BindView(R.id.layout_shop_coupon)
    LinearLayout layoutShopCoupon;
    @BindView(R.id.layout_shop_type)
    LinearLayout layoutShopType;
    @BindView(R.id.gv_shop_hot)
    MyGridView gvShopHot;
    @BindView(R.id.gv_shop_recommand_good)
    MyGridView gvShopRecommandGood;
    @BindView(R.id.gv_shop_recommand)
    MyGridView gvShopRecommand;
    @BindView(R.id.banner_shop2)
    ConvenientBanner bannerShop2;
    @BindView(R.id.gv_shop_hot_more)
    MyGridView gvShopHotMore;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_consumption, null);
        }
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @OnClick({R.id.layout_shop_myaccount,R.id.layout_shop_myorder})
    void click(View v){
        switch (v.getId()){
            case R.id.layout_shop_myaccount:
                startActivity(new Intent(mContext, ShopInfoActivity.class));
                break;
            case R.id.layout_shop_myorder:
                startActivity(new Intent(mContext, GoodInfoActivity.class));
                break;
        }
    }

}
