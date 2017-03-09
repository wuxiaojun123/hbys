package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.GoodFragment;
import com.help.reward.fragment.GoodImgInfoFragment;
import com.help.reward.fragment.GoodRetedFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MXY on 2017/3/3.
 */

public class GoodInfoActivity extends BaseActivity {
    @BindView(R.id.iv_goodinfo_back)
    ImageView ivGoodinfoBack;
    @BindView(R.id.iv_goodinfo_more)
    ImageView ivGoodinfoMore;
    @BindView(R.id.pstb_goodinfo)
    PagerSlidingTabStrip pstbGoodinfo;
    @BindView(R.id.layout_goodinfo_title)
    RelativeLayout layoutGoodinfoTitle;
    @BindView(R.id.layout_goodinfo_bottom)
    LinearLayout layoutGoodinfoBottom;
    @BindView(R.id.layout_spill)
    LinearLayout layoutSpill;
    @BindView(R.id.vp_goodinfo)
    ViewPager vpGoodinfo;

    private String goodsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodinfo);
        ButterKnife.bind(this);
        goodsId = getIntent().getStringExtra("goods_id");

        vpGoodinfo.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pstbGoodinfo.setViewPager(vpGoodinfo);
        vpGoodinfo.setOffscreenPageLimit(3);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.good_lable14);
            TITLES[1] = getResources().getString(R.string.good_lable15);
            TITLES[2] = getResources().getString(R.string.good_lable16);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if (position == 0) {
                GoodFragment goodFragment = new GoodFragment();
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goodsId);
                goodFragment.setArguments(bundle);
                return goodFragment;
            } else if (position == 1) {
                return new GoodImgInfoFragment();
            } else {
                return new GoodRetedFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
