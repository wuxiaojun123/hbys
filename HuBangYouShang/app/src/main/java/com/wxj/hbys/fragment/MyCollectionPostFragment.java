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
import com.wxj.hbys.adapter.IntegrationWatchPraiseAdapter;
import com.wxj.hbys.adapter.MyCollectionPostAdapter;
import com.wxj.hbys.adapter.MyRewardPostAdapter;
import com.wxj.hbys.bean.MyCollectionPostBean;
import com.wxj.hbys.bean.Response.BaseResponse;
import com.wxj.hbys.bean.Response.MyCollectionPostResponse;
import com.wxj.hbys.bean.Response.MyRewardPostResponse;
import com.wxj.hbys.minterface.OnItemDeleteListener;
import com.wxj.hbys.network.PersonalNetwork;
import com.wxj.hbys.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * item_my_collection_post
 *
 * Created by wuxiaojun on 2017/2/11.
 */

public class MyCollectionPostFragment extends BaseFragment {

    private int numSize = 15;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyCollectionPostAdapter mCollectionPostAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_collection_post;
    }


    @Override
    protected void init() {
        initNetwork();
        initRecycler();
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mCollectionPostAdapter = new MyCollectionPostAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mCollectionPostAdapter);
        lRecyclerview.setAdapter(adapter);
        initDeleteListener();
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initDeleteListener() {
        mCollectionPostAdapter.setOnItemDeleteListener(new OnItemDeleteListener() {
            @Override
            public void deleteItem(final int position) {
                MyCollectionPostBean bean = (MyCollectionPostBean) mCollectionPostAdapter.getDataList().get(position);
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
                                        mCollectionPostAdapter.remove(position);
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
                .getMyCollectionPostResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyCollectionPostResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyCollectionPostResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                mCollectionPostAdapter.addAll(response.data.favorites_list);
                            }
                            if(!response.hasmore){
                                lRecyclerview.setLoadMoreEnabled(false);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
