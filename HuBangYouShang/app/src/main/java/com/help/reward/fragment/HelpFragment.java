package com.help.reward.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.activity.HelpFilterActivity;
import com.help.reward.activity.MsgCenterActivity;
import com.help.reward.activity.ReleaseActivity;
import com.help.reward.activity.SearchHelpActivity;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.UpdateMessageDataRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.SearchEditTextView;
import com.idotools.utils.InputWindowUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

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
    SearchEditTextView etHelpTitle;
    @BindView(R.id.pstb_help)
    PagerSlidingTabStrip pstbHelp;
    @BindView(R.id.vp_help)
    ViewPager vpHelp;
    private View contentView;
    String board_id, board_name;
    String area_id, area_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_help, null);
        }
        ButterKnife.bind(this, contentView);

        if (helpSeekFragment == null) {
            helpSeekFragment = new HelpSeekFragment();
            Bundle bundle = new Bundle();
            bundle.putString("area_id", area_id);
            bundle.putString("board_id", board_id);
            helpSeekFragment.setArguments(bundle);
        }
        if (helpRewardsFragment == null) {
            helpRewardsFragment = new HelpRewardsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("area_id", area_id);
            bundle.putString("board_id", board_id);
            helpRewardsFragment.setArguments(bundle);
        }
        if (helpVoteFragment == null) {
            helpVoteFragment = new HelpVoteFragment();
            Bundle bundle = new Bundle();
            bundle.putString("area_id", area_id);
            bundle.putString("board_id", board_id);
            helpVoteFragment.setArguments(bundle);
        }
        vpHelp.setOffscreenPageLimit(3);
        vpHelp.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        pstbHelp.setViewPager(vpHelp);
        etHelpTitle.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                search();
            }
        });
        updateData();
        return contentView;
    }

    private void search() {
        String searchStr = etHelpTitle.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            InputWindowUtils.closeInputWindow(etHelpTitle, mContext);
            Intent intent = new Intent(mContext, SearchHelpActivity.class);
            intent.putExtra("keyword", searchStr);
            mContext.startActivity(intent);
            ActivitySlideAnim.slideInAnim(getActivity());
        } else {
            ToastUtils.show(mContext, "请输入搜索内容");
        }
    }

    @OnClick({R.id.layout_help_titleleft, R.id.layout_help_title_eidt, R.id.layout_help_title_sms})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_help_titleleft:
                Intent intent = new Intent(mContext, HelpFilterActivity.class);
                intent.putExtra("area_id", area_id);
                intent.putExtra("area_name", area_name);
                intent.putExtra("board_id", board_id);
                intent.putExtra("board_name", board_name);
                this.startActivityForResult(intent, 100);
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

                return helpSeekFragment;
            } else if (position == 1) {

                return helpRewardsFragment;
            } else {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (100 == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                board_id = data.getExtras().getString("board_id");
                area_id = data.getExtras().getString("area_id");
                board_name = data.getExtras().getString("board_name");
                area_name = data.getExtras().getString("area_name");
                if (helpSeekFragment != null) {
                    helpSeekFragment.setSelectInfo(board_id, area_id);
                }
                if (helpRewardsFragment != null) {
                    helpRewardsFragment.setSelectInfo(board_id, area_id);
                }
                if (helpVoteFragment != null) {
                    helpVoteFragment.setSelectInfo(board_id, area_id);
                }
            }
        }
    }

    /***
     * 更新消息红点
     */
    private void updateData() {
        RxBus.getDefault().toObservable(UpdateMessageDataRxbusType.class).subscribe(new Action1<UpdateMessageDataRxbusType>() {
            @Override
            public void call(UpdateMessageDataRxbusType type) {
                if (type.hasNew) { // 更新数据
                    tvTitleHelpMsgcount.setVisibility(View.VISIBLE);
                }else{
                    tvTitleHelpMsgcount.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
