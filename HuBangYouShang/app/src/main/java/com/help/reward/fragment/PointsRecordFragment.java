package com.help.reward.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyRewardCommentAdapter;
import com.help.reward.adapter.PointsRecordAdapter;
import com.help.reward.bean.Response.MyRewardCommentResponse;
import com.help.reward.bean.Response.PointsRecordResponse;
import com.help.reward.network.CouponPointsNetwork;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的获赏--跟帖
 * Created by wuxiaojun on 2017/2/8.
 */

public class PointsRecordFragment extends BaseFragment {

    private int numSize = 15;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private PointsRecordAdapter mHelpPostAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_reward_post;
    }

    @Override
    protected void init() {
        initRecyclerView();
        initNetwork();
    }

    private void initNetwork() {
        CouponPointsNetwork
                .getHelpNoCookieApi()
                .receivePointsLog(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PointsRecordResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(PointsRecordResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                mHelpPostAdapter.setDataList(response.data);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mHelpPostAdapter = new PointsRecordAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHelpPostAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        initRefreshListener();
        lRecyclerview.setLoadMoreEnabled(false);
//        initLoadMoreListener();
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }


}
