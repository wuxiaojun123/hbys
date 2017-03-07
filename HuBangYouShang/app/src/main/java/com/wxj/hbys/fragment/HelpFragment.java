package com.wxj.hbys.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.wxj.hbys.R;
import com.wxj.hbys.activity.HelpFilterActivity;
import com.wxj.hbys.activity.MsgCenterActivity;
import com.wxj.hbys.activity.ReleaseActivity;
import com.wxj.hbys.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页-互帮
 * Created by wuxiaojun on 2017/1/8.
 */

public class HelpFragment extends BaseFragment {

    @BindView(R.id.layout_help_titleleft)
    LinearLayout layoutHelpTitleleft;
    @BindView(R.id.layout_help_title_eidt)
    LinearLayout layoutHelpTitleEidt;
    @BindView(R.id.tv_title_help_msgcount)
    TextView tvTitleHelpMsgcount;
    @BindView(R.id.et_help_title)
    EditText etHelpTitle;
    @BindView(R.id.pstb_help)
    PagerSlidingTabStrip pstbHelp;
    @BindView(R.id.vp_help)
    ViewPager vpHelp;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_help, null);
        }
        ButterKnife.bind(this, contentView);
        vpHelp.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        pstbHelp.setViewPager(vpHelp);
        return contentView;
    }

    @OnClick({R.id.layout_help_titleleft, R.id.layout_help_title_eidt, R.id.layout_help_title_sms})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_help_titleleft:
                startActivity(new Intent(mContext, HelpFilterActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.layout_help_title_eidt:
                startActivity(new Intent(mContext, ReleaseActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;
            case R.id.layout_help_title_sms:
                startActivity(new Intent(mContext, MsgCenterActivity.class));
                ActivitySlideAnim.slideInAnim(getActivity());

                break;

        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = new String[3];

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            TITLES[0] = getResources().getString(R.string.help_lable3);
            TITLES[1] = getResources().getString(R.string.help_lable4);
            TITLES[2] = getResources().getString(R.string.help_lable5);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            // 下面两个fragment是个人中心里的
            if (position == 0) {
                return new HelpSeekFragment();
            } else if (position == 1) {
                return new HelpRewardsFragment();
            } else {
                return new HelpVoteFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
