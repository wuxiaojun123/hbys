package com.help.reward.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
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
import com.help.reward.R;
import com.help.reward.activity.HelpFilterActivity;
import com.help.reward.activity.MsgCenterActivity;
import com.help.reward.activity.ReleaseActivity;
import com.help.reward.utils.ActivitySlideAnim;

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
        vpHelp.setOffscreenPageLimit(3);
        vpHelp.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
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

    HelpSeekFragment helpSeekFragment;
    HelpRewardsFragment helpRewardsFragment;
    HelpVoteFragment helpVoteFragment;
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
                if (helpSeekFragment == null) {
                    helpSeekFragment = new HelpSeekFragment();
                }
                return helpSeekFragment;
            } else if (position == 1) {
                if (helpRewardsFragment == null) {
                    helpRewardsFragment = new HelpRewardsFragment();
                }
                return helpRewardsFragment;
            } else {
                if(helpVoteFragment==null){
                    helpVoteFragment = new HelpVoteFragment();
                }
                return helpVoteFragment;
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
