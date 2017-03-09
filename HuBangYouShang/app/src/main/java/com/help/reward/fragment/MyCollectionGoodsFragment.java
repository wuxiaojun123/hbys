package com.help.reward.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyCollectionGoodsAdapter;
import com.help.reward.bean.MyCollectionPostBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.MyCollectionGoodsResponse;
import com.help.reward.minterface.OnItemDeleteListener;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/11.
 */

public class MyCollectionGoodsFragment extends BaseFragment {


    private int numSize = 15;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyCollectionGoodsAdapter mCollectionGoodsAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_collection_goods;
    }

    @Override
    protected void init() {
        initNetwork();
        initRecycler();
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mCollectionGoodsAdapter = new MyCollectionGoodsAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(mCollectionGoodsAdapter);
        lRecyclerview.setAdapter(adapter);
        initDeleteListener();
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initDeleteListener() {
        mCollectionGoodsAdapter.setOnItemDeleteListener(new OnItemDeleteListener() {
            @Override
            public void deleteItem(final int position) {
                MyCollectionPostBean bean = (MyCollectionPostBean) mCollectionGoodsAdapter.getDataList().get(position);
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
                                        mCollectionGoodsAdapter.remove(position);
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
                .getMyCollectionGoodsResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyCollectionGoodsResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyCollectionGoodsResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            LogUtils.e("获取数据成功。。。" + response.data.favorites_list.size());
                            if (response.data != null) {
                                mCollectionGoodsAdapter.addAll(response.data.favorites_list);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
