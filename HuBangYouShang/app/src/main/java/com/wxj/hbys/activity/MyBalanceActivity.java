package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.wxj.hbys.R;
import com.wxj.hbys.fragment.BaseFragment;
import com.wxj.hbys.fragment.MyAccountHelpRewardFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyBalanceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;


    private MyFragmentPageAdapter mAdapter;
    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);
        ButterKnife.bind(this);

        initEvent();
        initData();
    }

    private void initData() {
        fragmentList = new ArrayList<>(3);
        fragmentList.add(new MyAccountHelpRewardFragment());
        fragmentList.add(new MyAccountHelpRewardFragment());
        fragmentList.add(new MyAccountHelpRewardFragment());
        viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }

    private void initEvent() {

    }

    @OnClick({R.id.tv_balance_recharge, R.id.tv_help_score, R.id.tv_general_volume})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_balance_recharge:
                // 余额充值
                startActivity(new Intent(MyBalanceActivity.this, PrepaidBalanceActivity.class));

                break;
            case R.id.tv_help_score:
                // 兑换帮赏分
                startActivity(new Intent(MyBalanceActivity.this, BalanceExchangeHelpScoreActivity.class));

                break;
            case R.id.tv_general_volume:
                // 兑换通用卷
                startActivity(new Intent(MyBalanceActivity.this, BalanceExchangeVolumeActivity.class));

                break;
        }
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
