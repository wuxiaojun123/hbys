package com.help.reward.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpRewardInfoActivity;
import com.help.reward.adapter.CouponRecordAdapter;
import com.help.reward.adapter.MyRewardCommentAdapter;
import com.help.reward.bean.GroupCouponsRecordResponse;
import com.help.reward.bean.Response.CouponsRecordResponse;
import com.help.reward.bean.Response.MyRewardCommentResponse;
import com.help.reward.network.CouponPointsNetwork;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的获赏--跟帖
 * Created by wuxiaojun on 2017/2/8.
 */

public class CouponsRecordFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private CouponRecordAdapter mCouponRecordAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private String groupId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_reward_post;
    }

    @Override
    protected void init() {
        groupId = getArguments().getString("groupId");
        initRecyclerView();
        initNetwork();
    }

    private void initNetwork() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
        // mobile/index.php?act=member_voucher&op=receiveVoucherLog
        // /mobile/index.php?act=index&op=giveVoucherLog 群发放记录
        CouponPointsNetwork
                .getHelpNoCookieApi()
                .groupGrantCouponsLog("index", "giveVoucherLog", currentPage + "", groupId, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GroupCouponsRecordResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(GroupCouponsRecordResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (currentPage == 1) {
                                    mCouponRecordAdapter.setDataList(response.data.giveList);
                                } else {
                                    mCouponRecordAdapter.addAll(response.data.giveList);
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


    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mCouponRecordAdapter = new CouponRecordAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mCouponRecordAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        initRefreshListener();
        initLoadMoreListener();
//        initOnItemClickListener();
    }

    /*private void initOnItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(mContext, HelpRewardInfoActivity.class);
                intent.putExtra("id", adapter.getDataList().get(position).id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());

            }
        });
    }*/

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetwork();
            }
        });
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                initNetwork();
            }
        });
    }


}
