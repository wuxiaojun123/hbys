package com.wxj.hbys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.recyclerview.LRecyclerView;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.wxj.hbys.App;
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.MyRewardPostAdapter;
import com.wxj.hbys.bean.Response.MyCollectionPostResponse;
import com.wxj.hbys.bean.Response.MyRewardPostResponse;
import com.wxj.hbys.network.PersonalNetwork;
import com.wxj.hbys.network.base.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/11.
 */

public class MyCollectionPostFragment extends BaseFragment {

    private int numSize = 15;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyRewardPostAdapter mHelpPostAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_collection_post;
    }


    @Override
    protected void init() {
        initNetwork();
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
                            LogUtils.e("获取数据成功。。。" + response.data.favorites_list.size());
                            if (response.data != null) {
//                                mHelpPostAdapter.addAll(response.data);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
