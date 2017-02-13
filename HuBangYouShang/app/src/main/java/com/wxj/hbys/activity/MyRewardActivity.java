package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.wxj.hbys.R;
import com.wxj.hbys.fragment.MyHelpCommentFragment;
import com.wxj.hbys.fragment.MyHelpPostFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyRewardActivity extends BaseActivity {

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward);
        ButterKnife.bind(this);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter{

        private String[] TITLES = new String[2];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.string_replies);
            TITLES[1] = getResources().getString(R.string.string_comment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
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
