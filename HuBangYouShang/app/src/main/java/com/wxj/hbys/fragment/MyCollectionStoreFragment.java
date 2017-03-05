package com.wxj.hbys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.wxj.hbys.App;
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.MyCollectionGoodsAdapter;
import com.wxj.hbys.adapter.MyCollectionStoreAdapter;
import com.wxj.hbys.adapter.MyRewardPostAdapter;
import com.wxj.hbys.bean.MyCollectionPostBean;
import com.wxj.hbys.bean.Response.BaseResponse;
import com.wxj.hbys.bean.Response.MyCollectionGoodsResponse;
import com.wxj.hbys.bean.Response.MyCollectionStoreResponse;
import com.wxj.hbys.minterface.OnItemDeleteListener;
import com.wxj.hbys.network.PersonalNetwork;
import com.wxj.hbys.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/11.
 */

public class MyCollectionStoreFragment extends BaseFragment {

    private int numSize = 15;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyCollectionStoreAdapter myCollectionStoreAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_collection_store;
    }


    @Override
    protected void init() {
        initNetwork();
        initRecycler();
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        myCollectionStoreAdapter = new MyCollectionStoreAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(myCollectionStoreAdapter);
        lRecyclerview.setAdapter(adapter);
        initDeleteListener();
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initDeleteListener() {
        myCollectionStoreAdapter.setOnItemDeleteListener(new OnItemDeleteListener() {
            @Override
            public void deleteItem(final int position) {
                MyCollectionPostBean bean = (MyCollectionPostBean) myCollectionStoreAdapter.getDataList().get(position);
                if(bean != null){
                    PersonalNetwork
                            .getResponseApi()
                            .getDeleteMyCollectionPostResponse(App.APP_CLIENT_KEY,bean.fav_id,bean.log_msg)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseSubscriber<BaseResponse>() {
                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    ToastUtils.show(mContext, R.string.string_error);
                                }

                                @Override
                                public void onNext(BaseResponse response) {
                                    LogUtils.e("删除我的收藏中的帖子："+response.toString());
                                    if (response.code == 200) { // 删除成功
                                        myCollectionStoreAdapter.remove(position);
                                    } else {
                                        ToastUtils.show(mContext, response.msg);
                                    }
                                }
                            });

                }
            }
        });
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
                LogUtils.e("执行下拉刷新的方法");
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getMyCollectionStoreResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyCollectionStoreResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyCollectionStoreResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            LogUtils.e("获取数据成功。。。" + response.data.favorites_list.size());
                            if (response.data != null) {
                                myCollectionStoreAdapter.addAll(response.data.favorites_list);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }



}
