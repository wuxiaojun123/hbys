package com.help.reward.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.bean.MyCollectionStoreBean;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyCollectionStoreAdapter;
import com.help.reward.bean.MyCollectionPostBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.MyCollectionStoreResponse;
import com.help.reward.minterface.OnItemDeleteListener;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/11.
 */

public class MyCollectionStoreFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;

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
                MyCollectionStoreBean bean = (MyCollectionStoreBean) myCollectionStoreAdapter.getDataList().get(position);
                if(bean != null){
                    PersonalNetwork
                            .getResponseApi()
                            .getDeleteMyCollectionStoreResponse(App.APP_CLIENT_KEY,bean.store_id)
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
                                    LogUtils.e("删除我的收藏中的店铺："+response.toString());
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

    private void initNetwork() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
        //?act=member_favorites_store&op=favorites_list
        PersonalNetwork
                .getResponseApi()
                .getMyCollectionStoreResponse("member_favorites_store","favorites_list",currentPage+"",App.APP_CLIENT_KEY)
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
                                if(currentPage == 1){
                                    myCollectionStoreAdapter.setDataList(response.data.favorites_list);
                                }else{
                                    myCollectionStoreAdapter.addAll(response.data.favorites_list);
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



}
