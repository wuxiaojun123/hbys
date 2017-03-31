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
import com.help.reward.adapter.CouponTradingAdapter;
import com.help.reward.bean.Response.CouponTradingResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 优惠劵交易大厅--从高到低--从低到高
 * Created by wuxiaojun on 2017/1/15.
 */

public class CouponTradingFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;
    private String order = "asc";
    private String storeName = "0";

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private CouponTradingAdapter mCollectionGoodsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            order = bundle.getString("order");
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
        mCollectionGoodsAdapter = new CouponTradingAdapter(mContext);
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
     * mobile/index.php?act=voucher&op=voucher_list
     */
    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getCouponTradingResponse("voucher", "voucher_list", "" + currentPage, App.APP_CLIENT_KEY, storeName, order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CouponTradingResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(CouponTradingResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                mCollectionGoodsAdapter.addAll(response.data.voucher_list);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);
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
