package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpRewardInfoActivity;
import com.help.reward.adapter.HelpRewardsAdapter;
import com.help.reward.adapter.PersonHomepageSeekAdapter;
import com.help.reward.bean.HelpRewardsBean;
import com.help.reward.bean.HomepageBean;
import com.help.reward.bean.Response.HelpRewardsResponse;
import com.help.reward.bean.Response.HomepageHelpResponse;
import com.help.reward.bean.Response.HomepageSeekResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 互帮-获赏
 * Created by MXY on 2017/2/19.
 */

public class PersonHomepageSeekFragment extends BaseFragment {

    private View contentView;
    private PersonHomepageSeekAdapter adapter;
    @BindView(R.id.lv_fragment_help2)
    LRecyclerView lRecyclerview;
    private int numSize = 15;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    List<HomepageBean> mDatas = new ArrayList<>();
    int curpage = 1;
    @BindView(R.id.ll_empty)
    View ll_empty;
    @BindView(R.id.tv_no_result)
    TextView tv_no_result;
    String type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_help2, null);
        }
        ButterKnife.bind(this, contentView);
        initData();
        return contentView;
    }

    private void initData() {
        tv_no_result.setText("抱歉没有找到符合条件的帖子");
        if ("search".equalsIgnoreCase(type)) {
            lRecyclerview.setEmptyView(ll_empty);
        }
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new PersonHomepageSeekAdapter(mContext);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initRefreshListener();
        initLoadMoreListener();
        initItemClickListener();
        requestData();
    }


    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.e("执行下拉刷新的方法");
                curpage = 1;
                requestData();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                requestData();
            }
        });
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, HelpRewardInfoActivity.class);
                intent.putExtra("id", adapter.getDataList().get(position).id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }

        });
    }

    private void requestData() {

        subscribe = PersonalNetwork
                .getResponseApi()
                .getHomepageSeekResponse(App.APP_CLIENT_KEY, App.APP_USER_ID, "get_reward")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HomepageSeekResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1) {
                            curpage--;
                        }
                    }

                    @Override
                    public void onNext(HomepageSeekResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    adapter.setDataList(response.data.get_reward);
                                    if (adapter.getDataList().size() == 0) {
//                                        ToastUtils.show(mContext, "暂无数据");
                                    }
                                } else {
                                    adapter.addAll(response.data.get_reward);
                                }
                            }
                            if (!response.hasmore) {
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                lRecyclerview.setLoadMoreEnabled(true);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });


    }
}

