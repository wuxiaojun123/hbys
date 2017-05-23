package com.help.reward.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.activity.MsgCenterActivity;
import com.help.reward.activity.SearchAdvertisementActivity;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页-积分
 * <p>
 * item_ad.xml  看广告的item
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class IntegrationFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    @BindView(R.id.et_search)
    SearchEditTextView et_search; // 搜索内容
    @BindView(R.id.iv_email)
    ImageView iv_email; // 消息
    @BindView(R.id.tv_text)
    TextView tv_text; // 搜索

    private BaseFragment[] fragments = new BaseFragment[2];

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integration;
    }

    @Override
    protected void init() {
        initView();
        initViewPager();
    }

    private void initView() {
        iv_email.setVisibility(View.GONE);
        tv_text.setVisibility(View.VISIBLE);
        tv_text.setText("搜索");
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                search();
            }
        });
    }

    private void initViewPager() {
        fragments[0] = new IntegrationWatchPraiseFragment();
        fragments[1] = new IntegrationGroupBuyingFragment();
        viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }

    @OnClick({R.id.iv_search,R.id.tv_text})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_text:
            case R.id.iv_search:
                // 点击搜索
                search();
                break;
        }
    }

    private void search() {
        String searchStr = et_search.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            Intent mIntent = new Intent(getActivity(), SearchAdvertisementActivity.class);
            mIntent.putExtra("keyword", searchStr);
            getActivity().startActivity(mIntent);
            ActivitySlideAnim.slideInAnim(getActivity());
        } else {
            ToastUtils.show(mContext, "请输入搜索内容");
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[2];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.string_watch_point_praise);
            TITLES[1] = getResources().getString(R.string.string_group_buying);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if (position == 0) {
                return fragments[0];
            } else {
                return fragments[1];
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }


}
