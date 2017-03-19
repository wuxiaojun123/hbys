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
import com.help.reward.fragment.MyAccountHelpRewardFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 优惠劵交易大厅
 *   优惠劵详情--买家
 *   优惠劵详情--卖家
 *   item_coupon_trading.xml
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class CouponTradingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;


    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_trading);
        ButterKnife.bind(this);

        initEvent();
        initData();
    }

    private void initData() {
        fragmentList = new ArrayList<>(2);
        CouponTradingFragment fragment1 = new CouponTradingFragment();
        CouponTradingFragment fragment2 = new CouponTradingFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }

    private void initEvent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


        }

    }


    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getMString(R.string.string_meoney_from_high_to_low);
            } else {
                return getMString(R.string.string_meoney_from_low_to_high);
            }
        }

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private String getMString(int resId) {
        return mContext.getString(resId);
    }

}
