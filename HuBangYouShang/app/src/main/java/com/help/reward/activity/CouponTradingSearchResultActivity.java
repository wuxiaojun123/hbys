package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.CouponTradingFragment;
import com.help.reward.fragment.MyAccountHelpRewardFragment;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.GoodsTypeSearchPop;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 优惠劵交易大厅,搜索结果展示页
 * 优惠劵详情--买家
 * 优惠劵详情--卖家
 * item_coupon_trading.xml
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class CouponTradingSearchResultActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_search_type)
    TextView iv_search_type;
    @BindView(R.id.et_search)
    SearchEditTextView et_search;
    @BindView(R.id.tv_text)
    TextView tv_text;
    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    private String goodsname;
    private String storeName;

    private List<BaseFragment> fragmentList;
    private CouponTradingFragment ascFragment;
    private CouponTradingFragment descFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_trading_search_result);
        ButterKnife.bind(this);
        Intent mIntent = getIntent();
        goodsname = mIntent.getStringExtra("goodsname");
        storeName = mIntent.getStringExtra("storeName");
        if(!TextUtils.isEmpty(goodsname)){
            et_search.setText(goodsname);
        }else{
            et_search.setText(storeName); // 如果是店铺，那么标记也要是店铺
            et_search.setHint("搜索关键字相关店铺");
            iv_search_type.setText("店铺");
        }

        initData();
        initEvent();
    }

    private void initEvent() {
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                String keyword = et_search.getText().toString().trim();
                if (!StringUtils.checkStr(keyword)) {
                    return;
                }
                LogUtils.e("输入的搜索内容是：" + keyword);
                if ("商品".equals(iv_search_type.getText().toString())) {
                    goodsname = keyword;
                    storeName = "";
                } else { //搜索店铺
                    goodsname = "";
                    storeName = keyword;
                }
                // 通知fragment页面搜索变成了店铺或者是商品
                ascFragment.reset(1,storeName,goodsname);
                descFragment.reset(1,storeName,goodsname);
            }
        });
    }

    private void initData() {
        fragmentList = new ArrayList<>(2);
        ascFragment = new CouponTradingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("order", "asc");
        bundle.putString("goodsname", goodsname);
        bundle.putString("storeName", storeName);
        ascFragment.setArguments(bundle);

        descFragment = new CouponTradingFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("order", "desc");
        bundle2.putString("goodsname", goodsname);
        bundle2.putString("storeName", storeName);
        descFragment.setArguments(bundle2);

        fragmentList.add(ascFragment);
        fragmentList.add(descFragment);
        viewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }


    @OnClick({R.id.iv_search_type, R.id.tv_text})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_text: // 取消
                finish();
                ActivitySlideAnim.slideOutAnim(CouponTradingSearchResultActivity.this);

                break;
            case R.id.iv_search_type: // 弹窗popupwindow
                new GoodsTypeSearchPop().showPopupWindow(this, iv_search_type).setOnTypeChooseListener(new GoodsTypeSearchPop.OnTypeChooseListener() {
                    @Override
                    public void onType(String type) {
                        if ("goods".equals(type)) {
                            et_search.setHint("搜索关键字相关商品");
                            iv_search_type.setText("商品");
                            storeName = "";
                        } else {
                            et_search.setHint("搜索关键字相关店铺");
                            iv_search_type.setText("店铺");
                            goodsname = "";
                        }
                    }
                });
                break;
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
