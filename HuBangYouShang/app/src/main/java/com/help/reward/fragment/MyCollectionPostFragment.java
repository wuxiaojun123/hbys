package com.help.reward.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.activity.HelpRewardInfoActivity;
import com.help.reward.activity.HelpSeekInfoActivity;
import com.help.reward.minterface.OnMyItemClickListener;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyCollectionPostAdapter;
import com.help.reward.bean.MyCollectionPostBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.MyCollectionPostResponse;
import com.help.reward.minterface.OnItemDeleteListener;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * item_my_collection_post
 * <p>
 * Created by wuxiaojun on 2017/2/11.
 */

public class MyCollectionPostFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyCollectionPostAdapter mCollectionPostAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

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
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mCollectionPostAdapter);
        lRecyclerview.setAdapter(lRecyclerViewAdapter);
        initDeleteListener();
        initRefreshListener();
        initLoadMoreListener();
        initOnItemClickListener();
    }

    private void initDeleteListener() {
        mCollectionPostAdapter.setOnItemDeleteListener(new OnItemDeleteListener() {
            @Override
            public void deleteItem(final int position) {
                MyCollectionPostBean bean = (MyCollectionPostBean) mCollectionPostAdapter.getDataList().get(position);
                if (bean != null) {
                    PersonalNetwork
                            .getResponseApi()
                            .getDeleteMyCollectionPostResponse(App.APP_CLIENT_KEY, bean.fav_id, bean.log_msg)
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
                                    LogUtils.e("删除我的收藏中的帖子：" + response.toString());
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

    private void initOnItemClickListener() {
        mCollectionPostAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
            @Override
            public void onMyItemClickListener(int position) {
                MyCollectionPostBean bean = mCollectionPostAdapter.getDataList().get(position);
                Intent intent = null;
                if (bean.log_msg.equals("reward")) { // 获赏
                    intent = new Intent(mContext, HelpRewardInfoActivity.class);
                } else { // 求助
                    intent = new Intent(mContext, HelpSeekInfoActivity.class);
                }
                intent.putExtra("id", bean.log_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());
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
        // ?act=member_favorites_post&op=favorites_list
        PersonalNetwork
                .getResponseApi()
                .getMyCollectionPostResponse("member_favorites_post", "favorites_list", currentPage + "", App.APP_CLIENT_KEY)
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
                                if (currentPage == 1) {
                                    mCollectionPostAdapter.setDataList(response.data.favorites_list);
                                } else {
                                    mCollectionPostAdapter.addAll(response.data.favorites_list);
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

    public void refreshRecycler() {
        if (mCollectionPostAdapter != null) {
            mCollectionPostAdapter.clear();
        }
    }

    public MyCollectionPostAdapter getMyCollectionPostAdapter() {
        return mCollectionPostAdapter;
    }

}
