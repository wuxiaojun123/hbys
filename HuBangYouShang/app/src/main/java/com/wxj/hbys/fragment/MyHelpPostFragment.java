package com.wxj.hbys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.base.recyclerview.ProgressStyle;
import com.idotools.utils.LogUtils;
import com.wxj.hbys.App;
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.MyHelpPostAdapter;
import com.wxj.hbys.bean.HelpPostBean;
import com.wxj.hbys.bean.Response.BaseResponse;
import com.wxj.hbys.bean.Response.MyHelpPostResponse;
import com.wxj.hbys.network.BaseSubscriber;
import com.wxj.hbys.network.PersonalNetwork;
import com.wxj.hbys.network.api.PersonalApi;
import com.wxj.hbys.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyHelpPostFragment extends BaseFragment {

    private int numSize = 15;

    @BindView(R.id.id_recycler_view)
    LRecyclerView mRecyclerView;
    private MyHelpPostAdapter mHelpPostAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<HelpPostBean> helpPostBeenList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_help_post, null);
        ButterKnife.bind(this, view);

        // 设置样式
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mHelpPostAdapter = new MyHelpPostAdapter(mContext);
        helpPostBeenList = new ArrayList<>();

        mHelpPostAdapter.addAll(helpPostBeenList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHelpPostAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        initRefreshListener();
        initLoadMoreListener();
        mRecyclerView.refresh();

        PersonalNetwork
                .getMyHelpPostResponseApi()
                .getMyHelpPostResponse("post",App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.e("请求出错");
                        mRecyclerView.refreshComplete(numSize);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        mRecyclerView.refreshComplete(numSize);
                        LogUtils.e("请求成功：" + response + "  数据是：" + response.toString());
                    }
                });

        return view;
    }

    private void initLoadMoreListener() {
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initRefreshListener() {
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }


}
