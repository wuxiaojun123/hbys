package com.wxj.hbys.activity;

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
import com.wxj.hbys.R;
import com.wxj.hbys.fragment.MyCollectionGoodsFragment;
import com.wxj.hbys.fragment.MyCollectionPostFragment;
import com.wxj.hbys.fragment.MyCollectionStoreFragment;
import com.wxj.hbys.fragment.MyHelpCommentFragment;
import com.wxj.hbys.fragment.MyHelpPostFragment;
import com.wxj.hbys.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的收藏
 * <p>
 * Created by wuxiaojun on 2017/2/8.
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_my_collection_title);
        tv_title_right.setText(R.string.string_my_clean);
        initViewPager();
    }

    private void initViewPager() {
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyCollectionActivity.this);

                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.string_post);
            TITLES[1] = getResources().getString(R.string.string_goods);
            TITLES[2] = getResources().getString(R.string.string_store);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MyCollectionPostFragment();
            } else if (position == 1) {
                return new MyCollectionGoodsFragment();
            } else {
                return new MyCollectionStoreFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }


}
