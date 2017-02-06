package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.wxj.hbys.R;
import com.wxj.hbys.fragment.BaseFragment;
import com.wxj.hbys.fragment.MyAccountHelpRewardFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帮赏分兑换
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyAccountHelpRewardActivity extends BaseActivity {

    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.id_tablayout)
    TabLayout mTabLayout;

    private MyFragmentPageAdapter mAdapter;
    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_help_reward);
        ButterKnife.bind(this);

        initEvent();
        initData();
    }

    private void initData() {
        fragmentList = new ArrayList<>(3);
        fragmentList.add(new MyAccountHelpRewardFragment());
        fragmentList.add(new MyAccountHelpRewardFragment());
        fragmentList.add(new MyAccountHelpRewardFragment());
        mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initEvent() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getMString(R.string.string_all);
            } else if (position == 1) {
                return getMString(R.string.string_expenditure);
            } else {
                return getMString(R.string.string_support);
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
