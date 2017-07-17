package com.help.reward.activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.BaseFragment;
import com.help.reward.fragment.PersonHomepageHelpFragment;
import com.help.reward.fragment.PersonHomepageSeekFragment;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.HomepageMemInfoRxbusType;
import com.help.reward.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 个人主页
 * Created by wuxiaojun on 17-7-17.
 */

public class PersonHomepageActivity extends BaseActivity {

    @BindView(R.id.iv_photo)
    ImageView iv_photo;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_subscibe)
    TextView tv_subscibe;

    @BindView(R.id.tv_help_count)
    TextView tv_help_count; // 帮助人数
    @BindView(R.id.tv_complaint)
    TextView tv_complaint; // 投诉率
    @BindView(R.id.tv_complainted)
    TextView tv_complainted; // 被投诉率

    @BindView(R.id.id_viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    private BaseFragment[] fragments = new BaseFragment[2];
    private PersonHomepageSeekFragment seekFragment;
    private PersonHomepageHelpFragment helpFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_homepage);
        ButterKnife.bind(this);


        String memberId = getIntent().getStringExtra("u_id");
        if(!TextUtils.isEmpty(memberId)){
            initData(memberId);
        }

    }

    private void initData(String memberId) {
        initRus();
        Bundle bundle = new Bundle();
        bundle.putString("member_id",memberId);

        PersonHomepageSeekFragment seekFragment = new PersonHomepageSeekFragment();
        seekFragment.setArguments(bundle);

        PersonHomepageHelpFragment helpFragment = new PersonHomepageHelpFragment();
        helpFragment.setArguments(bundle);
        fragments[0] = seekFragment;
        fragments[1] = helpFragment;
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        tabStrip.setViewPager(viewPager);

    }

    private void initRus() {
        RxBus.getDefault().toObservable(HomepageMemInfoRxbusType.class).subscribe(new Action1<HomepageMemInfoRxbusType>() {
            @Override
            public void call(HomepageMemInfoRxbusType memInfo) {
                if(memInfo != null){
                    GlideUtils.loadCircleImage(memInfo.member_avatar,iv_photo);
                    tv_title.setText(memInfo.member_name);
                    tv_subscibe.setText(memInfo.description);
                    tv_help_count.setText(memInfo.help_people);
                    tv_complaint.setText(memInfo.complaint);
                    tv_complainted.setText(memInfo.complained);
                }
            }
        });

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[2];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.help_lable3);
            TITLES[1] = getResources().getString(R.string.help_lable4);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if (position == 0) {

                return helpFragment;
            } else {

                return seekFragment;
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer != null)
                super.unregisterDataSetObserver(observer);
        }
    }

}
