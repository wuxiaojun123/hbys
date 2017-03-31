package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.GoodInfoActivity;
import com.help.reward.adapter.StoreGoodsAdapter;
import com.help.reward.bean.Response.StoreDetailNewResponse;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.network.StoreDetailNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/26.
 */

public class ShopInfoNewFragment extends BaseFragment {

    private View contentView;
    LRecyclerView lRecyclerview;
    private StoreGoodsAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    List<ShopMallHotBean> mDatas = new ArrayList<>();
    String store_id;
    int curpage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_shopinfo_new, null);
        }
        lRecyclerview = (LRecyclerView) contentView.findViewById(R.id.id_stickynavlayout_innerscrollview);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("store_id")) {
            store_id = bundle.getString("store_id");
        }
        initData();
        return contentView;
    }

    private void initData() {

        GridLayoutManager mgr = new GridLayoutManager(mContext, 2);
        lRecyclerview.setLayoutManager(mgr);
        adapter = new StoreGoodsAdapter(mContext);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initLoadMoreListener();
        initItemClickListener();
        requestData();
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
                Intent intent = new Intent(mContext, GoodInfoActivity.class);
                intent.putExtra("goods_id", adapter.getDataList().get(position).goods_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }

        });
    }

    private void requestData() {

        subscribe = StoreDetailNetwork
                .getStroeApi()
                .getStoreDetailNewResponse(App.APP_CLIENT_KEY, "store_new_goods", store_id, curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StoreDetailNewResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(15);
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1)
                            curpage--;
                    }

                    @Override
                    public void onNext(StoreDetailNewResponse response) {
                        lRecyclerview.refreshComplete(15);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    adapter.setDataList(response.data.goods_list);
                                    if (adapter.getDataList().size() == 0) {
                                    }
                                } else {
                                    adapter.addAll(response.data.goods_list);
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
