package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.MyOrderAllFragment;
import com.help.reward.utils.ActivitySlideAnim;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    public static final String STATE_TYPE = "state_type";
    public static final String STATE_TYPE_DEFAULT = "";
    public static final String STATE_TYPE_NEW = "state_new"; // 未付款
    public static final String STATE_TYPE_SEND = "state_send"; // 待收货
    public static final String STATE_TYPE_NOEVAL = "state_noeval"; // 待评价
    public static final String STATE_TYPE_NOTAKES = "state_notakes"; // 退款、收货

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    private int firstPage; // 第一次进来显示哪一个页面
    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        firstPage = getIntent().getIntExtra("firstPage", 0);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        tv_title.setText(R.string.string_my_order_title);
        tv_title_right.setVisibility(View.GONE);
    }

    private void initData() {
        fragmentList = new ArrayList<>(5);
        MyOrderAllFragment allFragment = new MyOrderAllFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(STATE_TYPE, STATE_TYPE_DEFAULT);
        allFragment.setArguments(bundle1);

        MyOrderAllFragment fragment2 = new MyOrderAllFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(STATE_TYPE, STATE_TYPE_NEW);
        fragment2.setArguments(bundle2);

        MyOrderAllFragment fragment3 = new MyOrderAllFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(STATE_TYPE, STATE_TYPE_SEND);
        fragment3.setArguments(bundle3);

        MyOrderAllFragment fragment4 = new MyOrderAllFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString(STATE_TYPE, STATE_TYPE_NOEVAL);
        fragment4.setArguments(bundle4);

        MyOrderAllFragment fragment5 = new MyOrderAllFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putString(STATE_TYPE, STATE_TYPE_NOTAKES);
        fragment5.setArguments(bundle5);

        fragmentList.add(allFragment);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
        fragmentList.add(fragment5);
        MyFragmentPageAdapter myFragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentPageAdapter);
        tabStrip.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);
        // 显示那一个fragment
        viewPager.setCurrentItem(firstPage);
    }

    private void initEvent() {

    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyOrderActivity.this);

                break;
        }
    }


    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getMString(R.string.string_all_order);
            } else if (position == 1) {
                return getMString(R.string.string_stay_payment);
            } else if (position == 2) {
                return getMString(R.string.string_stay_goods_receipt);
            } else if (position == 3) {
                return getMString(R.string.string_stay_evaluate);
            } else {
                return getMString(R.string.string_refund);
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
