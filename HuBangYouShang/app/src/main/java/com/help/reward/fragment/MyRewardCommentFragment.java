package com.help.reward.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.activity.HelpRewardInfoActivity;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyRewardCommentAdapter;
import com.help.reward.bean.Response.MyRewardCommentResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的获赏--跟帖
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyRewardCommentFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyRewardCommentAdapter mHelpPostAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_reward_post;
    }

    @Override
    protected void init() {
        initRecyclerView();
        initNet();
    }

    private void initNet() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getMyRewardCommentResponse(currentPage + "","comment", App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyRewardCommentResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyRewardCommentResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (currentPage == 1) {
                                    mHelpPostAdapter.setDataList(response.data);
                                } else {
                                    mHelpPostAdapter.addAll(response.data);
                                }
                            }
                            if (!response.hasmore) {
                                lRecyclerview.setNoMore(true);
                            } else {
                                currentPage += 1;
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mHelpPostAdapter = new MyRewardCommentAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHelpPostAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        initRefreshListener();
        initLoadMoreListener();
        initOnItemClickListener();
    }

    private void initOnItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(mContext, HelpRewardInfoActivity.class);
                intent.putExtra("id", mHelpPostAdapter.getDataList().get(position).id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());

            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNet();
            }
        });
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                initNet();
            }
        });
    }


}
