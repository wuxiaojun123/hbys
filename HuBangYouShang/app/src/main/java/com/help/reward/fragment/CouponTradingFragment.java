package com.help.reward.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

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
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 优惠劵交易大厅--从高到低--从低到高
 * Created by wuxiaojun on 2017/1/15.
 */

public class CouponTradingFragment extends BaseFragment {

    private int numSize = 15; // 一页展示数量
    public int currentPage = 1; // 当前页
    private String order = "asc"; // 升序
    public String storeName = ""; // 店铺名称
    public String goodsname = ""; // 商品名称


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
                currentPage = 1;
                initNetwork();
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
                .getCouponTradingResultResponse("voucher", "voucher_list", "" + currentPage, App.APP_CLIENT_KEY, storeName, order, goodsname)
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
                                if (currentPage == 1) {
                                    mCollectionGoodsAdapter.clear();
                                    LogUtils.e("清除原来数据,返回集合长度是：" + response.data.voucher_list.size());
                                }
                                mCollectionGoodsAdapter.addAll(response.data.voucher_list);
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

    /***
     * 重置搜索条件
     */
    public void reset(int currentPage, String storeName, String goodsname) {
        this.currentPage = currentPage;
        this.storeName = storeName;
        this.goodsname = goodsname;
        initNetwork();
    }

}
