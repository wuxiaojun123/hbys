package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class MyHelpActivity extends BaseActivity {

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_help);
        ButterKnife.bind(this);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                return 2;
            }
        });
        tabStrip.setViewPager(viewPager);
    }


}
