package com.help.reward.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.help.reward.R;
import com.help.reward.adapter.viewholder.BannerImageHolderView;
import com.help.reward.adapter.viewholder.GuideImageHolderView;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.SharedPreferenceConstant;
import com.idotools.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuxiaojun on 2017/8/4.
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.banner_guide)
    ConvenientBanner convenientBanner;
    @BindView(R.id.id_enter)
    TextView id_enter;

    private List<Integer> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        mList.add(R.mipmap.img_guide_first);
        mList.add(R.mipmap.img_guide_second);
        mList.add(R.mipmap.img_guide_third);

        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new GuideImageHolderView();
            }
        }, mList)
                .setPageIndicator(new int[]{R.mipmap.img_ic_page_indicator, R.mipmap.img_ic_page_indicator_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setcurrentitem(0);
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 2) {
                    id_enter.setVisibility(View.GONE);
                } else {
                    id_enter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.id_enter})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_enter:
                // 设置标记
                SharedPreferencesHelper.getInstance(mContext).putBoolean(SharedPreferenceConstant.KEY_IS_FIRST, true);
                // 进入主页面
                Intent mIntent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(mIntent);
                finish();
                ActivitySlideAnim.slideInAnim(GuideActivity.this);

                break;
        }
    }

    @Override
    protected void setStatusBar() {
    }

    @Override
    public void onBackPressed() {
    }
}
