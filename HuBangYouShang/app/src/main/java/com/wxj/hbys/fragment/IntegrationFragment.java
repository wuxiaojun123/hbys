package com.wxj.hbys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.wxj.hbys.R;
import com.wxj.hbys.activity.MyRewardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页-积分
 *
 * item_ad.xml  看广告的item
 *
 * Created by wuxiaojun on 2017/1/8.
 */

public class IntegrationFragment extends BaseFragment {

    private View contentView;
    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentView == null){
            contentView = inflater.inflate(R.layout.fragment_integration,null);
        }
        ButterKnife.bind(this,contentView);

        viewPager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);

        return contentView;
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
            if(position == 0){
                return new MyHelpPostFragment();
            }else{
                return new MyHelpCommentFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }


}
