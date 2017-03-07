package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.wxj.hbys.R;
import com.wxj.hbys.fragment.ShopInfoAllFragment;
import com.wxj.hbys.fragment.ShopInfoHomeFragment;
import com.wxj.hbys.fragment.ShopInfoNewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 店铺信息
 * Created by MXY on 2017/2/26.
 */

public class StoreInfoActivity extends BaseActivity {

    @BindView(R.id.iv_shopinfo_title_back)
    ImageView ivShopinfoTitleBack;
    @BindView(R.id.iv_shopinfo_title_type)
    ImageView ivShopinfoTitleType;
    @BindView(R.id.et_shop_search)
    EditText etShopSearch;
    @BindView(R.id.iv_shopinfo_title_more)
    ImageView ivShopinfoTitleMore;
    @BindView(R.id.iv_shopinfo_bg)
    ImageView ivShopinfoBg;
    @BindView(R.id.pstb_shopinfo)
    PagerSlidingTabStrip pstbShopinfo;
    @BindView(R.id.iv_shopinfo_userimg)
    ImageView ivShopinfoUserimg;
    @BindView(R.id.tv_shop_funs)
    TextView tvShopFuns;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.vp_shopinfo)
    ViewPager vpShopinfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopinfo);
        ButterKnife.bind(this);
        vpShopinfo.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pstbShopinfo.setViewPager(vpShopinfo);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.shop_lable18);
            TITLES[1] = getResources().getString(R.string.shop_lable19);
            TITLES[2] = getResources().getString(R.string.shop_lable20);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if(position == 0){
                return new ShopInfoHomeFragment();
            }else if(position == 1){
                return new ShopInfoAllFragment();
            }
            else{
                return new ShopInfoNewFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
