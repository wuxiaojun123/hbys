package com.help.reward.activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.help.reward.R;
import com.help.reward.fragment.HelpRewardsFragment;
import com.help.reward.fragment.HelpSeekFragment;
import com.help.reward.fragment.HelpVoteFragment;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.SearchEditTextView;
import com.help.reward.view.SearchHistoryPop;
import com.idotools.utils.InputWindowUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * 搜索帖子
 * Created by wuxiaojun on 2017/3/22.
 */

public class SearchHelpActivity extends BaseActivity implements View.OnClickListener {
    protected Subscription subscribe;

    @BindView(R.id.iv_email)
    ImageView iv_email; // 隐藏
    @BindView(R.id.tv_text)
    TextView tv_text; // 取消
    @BindView(R.id.et_search)
    SearchEditTextView et_search; // 搜索内容

    @BindView(R.id.title_layout)
    View title_layout;
    @BindView(R.id.pstb_help)
    PagerSlidingTabStrip pstbHelp;
    @BindView(R.id.vp_help)
    ViewPager vpHelp;
    SearchHistoryPop searchHistoryPop;
    String searchStr;
    int nowTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchhelp);
        ButterKnife.bind(this);

        searchStr = getIntent().getStringExtra("keyword");
        initView();
        searchHistoryPop = new SearchHistoryPop(this, "searchhelp");
        searchHistoryPop.setOnHistoryChooseListener(new SearchHistoryPop.OnHistoryChooseListener(){
            @Override
            public void onHistory(String keyword) {
                et_search.setText(keyword);
                search();
            }
        });

    }

    private void initView() {
        iv_email.setVisibility(View.GONE);
        tv_text.setVisibility(View.VISIBLE);
        tv_text.setText("取消");
        et_search.setHint(R.string.help_lable2);
        et_search.setText(searchStr);
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                search();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    searchHistoryPop.showPopupWindow(title_layout);
                }
            }
        });
        vpHelp.setOffscreenPageLimit(3);
        vpHelp.setAdapter(new MyPagerAdapter(this.getSupportFragmentManager()));
        vpHelp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nowTab = position;
                if (nowTab == 0) {
                    helpSeekFragment.searchData(searchStr);
                } else if (nowTab == 1) {
                    helpRewardsFragment.searchData(searchStr);
                } else if (nowTab == 2) {
                    helpVoteFragment.searchData(searchStr);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pstbHelp.setViewPager(vpHelp);
    }


    @OnClick({R.id.iv_search, R.id.tv_text})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_search:
                search();
                break;
            case R.id.tv_text:
                finish();
                ActivitySlideAnim.slideOutAnim(SearchHelpActivity.this);
                break;

        }
    }

    private void search() {
        searchStr = et_search.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
//            initNetwor();
            searchHistoryPop.setKeyword(searchStr);
            InputWindowUtils.closeInputWindow(et_search, mContext);
            if (nowTab == 0) {
                helpSeekFragment.searchData(searchStr);
            } else if (nowTab == 1) {
                helpRewardsFragment.searchData(searchStr);
            } else if (nowTab == 2) {
                helpVoteFragment.searchData(searchStr);
            }
        } else {
            ToastUtils.show(mContext, "请输入搜索内容");
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
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "search");
                    bundle.putString("keyword", searchStr);
                    helpSeekFragment.setArguments(bundle);
                }
                return helpSeekFragment;
            } else if (position == 1) {
                if (helpRewardsFragment == null) {
                    helpRewardsFragment = new HelpRewardsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "search");
                    bundle.putString("keyword", searchStr);
                    helpRewardsFragment.setArguments(bundle);
                }
                return helpRewardsFragment;
            } else {
                if (helpVoteFragment == null) {
                    helpVoteFragment = new HelpVoteFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "search");
                    bundle.putString("keyword", searchStr);
                    helpVoteFragment.setArguments(bundle);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

}
