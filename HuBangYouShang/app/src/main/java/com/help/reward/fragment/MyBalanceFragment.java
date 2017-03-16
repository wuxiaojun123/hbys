package com.help.reward.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyAccountHelpRewardAdapter;
import com.help.reward.adapter.MyBalanceAdapter;
import com.help.reward.bean.Response.HelpRewardResponse;
import com.help.reward.bean.Response.MyBalanceResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.MyAccountHelpRewardRxbusType;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 余额明细-全部、支出、支入
 * Created by wuxiaojun on 2017/1/15.
 */

public class MyBalanceFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;
    private String requestType = "0";

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyBalanceAdapter mCollectionGoodsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            requestType = bundle.getString("type");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_account_help_reward;
    }

    @Override
    protected void init() {
        initRecycler();
        initNetwork();
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mCollectionGoodsAdapter = new MyBalanceAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mCollectionGoodsAdapter);
        lRecyclerview.setAdapter(adapter);
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetwork();
            }
        });
    }

    /***
     * ?act=member_fund&op=predepositlog
     */
    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getMyBalanceResponse("member_fund","predepositlog",""+currentPage,App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyBalanceResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyBalanceResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                mCollectionGoodsAdapter.addAll(response.data.list);
                                RxBus.getDefault().post(new MyAccountHelpRewardRxbusType(response.data.total_num));
                            }
                            if (currentPage == 1) {
                                lRecyclerview.setPullRefreshEnabled(false);
                            }
                            if (!response.hasmore) { // 是否有更多数据
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                currentPage += 1;
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
