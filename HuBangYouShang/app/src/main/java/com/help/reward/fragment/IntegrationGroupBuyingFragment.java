package com.help.reward.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.idotools.utils.ToastUtils;
import com.help.reward.R;
import com.help.reward.adapter.IntegrationIntegrationBuyingAdapter;
import com.help.reward.bean.Response.AdvertisementResponse;
import com.help.reward.network.IntegrationNetwork;
import com.help.reward.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 加群购买
 * Created by wuxiaojun on 17-3-1.
 */

public class IntegrationGroupBuyingFragment extends BaseFragment {

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;

    private int numSize = 15;
    private int currentPage = 1;
    private IntegrationIntegrationBuyingAdapter mIntegrationWatchPraiseAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integration_group_buying;
    }

    @Override
    protected void init() {
        initRecycler();
        initNetwor();
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mIntegrationWatchPraiseAdapter = new IntegrationIntegrationBuyingAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mIntegrationWatchPraiseAdapter);
        lRecyclerview.setAdapter(adapter);
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
                currentPage = 1;
                initNetwor();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetwor();
            }
        });
    }

    private void initNetwor() {
        subscribe = IntegrationNetwork
                .getIntegrationApi()
                .getAdvertisementGroupBuying("advertisement", ""+currentPage,"join")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AdvertisementResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AdvertisementResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                mIntegrationWatchPraiseAdapter.addAll(response.data.adv_list);
                            }
                            if (!response.hasmore) { // 是否有更多数据
                                lRecyclerview.setLoadMoreEnabled(false);
                            }else{
                                currentPage += 1;
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
