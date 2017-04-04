package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.CouponTradingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 优惠劵交易大厅
 *   item_coupon_trading.xml
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class CouponTradingActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_trading);
        ButterKnife.bind(this);

        initEvent();
        initData();
    }

    private void initData() {

    }

    private void initEvent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


        }

    }

}
