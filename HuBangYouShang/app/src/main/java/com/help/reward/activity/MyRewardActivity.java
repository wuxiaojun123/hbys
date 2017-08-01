package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.CouponsRecordFragment;
import com.help.reward.fragment.MyRewardCommentFragment;
import com.help.reward.fragment.MyRewardPostFragment;
import com.help.reward.fragment.PointsRecordFragment;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的获赏
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyRewardActivity extends BaseActivity implements View.OnClickListener {
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

    private PointsRecordFragment pointsRecordFragment;
    private CouponsRecordFragment couponsRecordFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward);
        ButterKnife.bind(this);

        initView();
        pointsRecordFragment = new PointsRecordFragment();
        couponsRecordFragment = new CouponsRecordFragment();

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }

    private void initView() {
        tv_title.setText(R.string.string_my_help_reward);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyRewardActivity.this);

                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[2];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.string_posted);
            TITLES[1] = getResources().getString(R.string.string_comment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new MyRewardPostFragment();
            }else{
                return new MyRewardCommentFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }


}
