package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.MyCouponFragment;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.MyAccountHelpRewardRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StatusBarUtil;
import com.idotools.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 优惠劵
 * item_my_coupon.xml
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyCouponActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tv_exchange)
    TextView tv_exchange;
    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;
    @BindView(R.id.rl_content)
    RelativeLayout rl_content;

    private Subscription subscribe;
    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        ButterKnife.bind(this);

        initData();
        getRxBusData();
    }

    private void initData() {
        fragmentList = new ArrayList<>(3);

        MyCouponFragment fragmentAll = new MyCouponFragment();
        //未使用
        MyCouponFragment fragmentPay = new MyCouponFragment();
        // 已使用
        Bundle bundle = new Bundle();
        bundle.putString("voucher_state", "2");
        fragmentPay.setArguments(bundle);

        MyCouponFragment fragmentInCome = new MyCouponFragment();
        // 已过期
        Bundle bundle2 = new Bundle();
        bundle2.putString("voucher_state", "3");
        fragmentInCome.setArguments(bundle2);

        fragmentList.add(fragmentAll);
        fragmentList.add(fragmentPay);
        fragmentList.add(fragmentInCome);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MyCouponActivity.this, StatusBarUtil.DEFAULT_ALPHA, null);
    }

    /**
     * 获取rxbus传递过来的数据
     */
    public void getRxBusData() {
        subscribe = RxBus.getDefault().toObservable(MyAccountHelpRewardRxbusType.class).subscribe(new Action1<MyAccountHelpRewardRxbusType>() {
            @Override
            public void call(MyAccountHelpRewardRxbusType type) {
                LogUtils.e("获取到信息" + type.points);
                tv_num.setText(type.points);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_exchange})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyCouponActivity.this);

                break;
            case R.id.tv_exchange:
                // 交易大厅
                startActivity(new Intent(MyCouponActivity.this, CouponTradingSearchResultActivity.class));
                ActivitySlideAnim.slideInAnim(MyCouponActivity.this);

                break;
        }
    }

    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getMString(R.string.string_weishiyong);
            } else if (position == 1) {
                return getMString(R.string.string_yishiyong);
            } else {
                return getMString(R.string.string_yiguoqi);
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

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
