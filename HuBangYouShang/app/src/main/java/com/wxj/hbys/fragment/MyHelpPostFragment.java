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
import com.wxj.hbys.R;
import com.wxj.hbys.adapter.MyHelpPostAdapter;
import com.wxj.hbys.bean.HelpPostBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyHelpPostFragment extends BaseFragment {

    @BindView(R.id.id_recycler_view)LRecyclerView mRecyclerView;
    private MyHelpPostAdapter mHelpPostAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<HelpPostBean> helpPostBeenList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_help_post,null);
        ButterKnife.bind(this,view);

        // 设置样式
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mHelpPostAdapter = new MyHelpPostAdapter(mContext);
        helpPostBeenList = new ArrayList<>();

        mHelpPostAdapter.addAll(helpPostBeenList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHelpPostAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        mRecyclerView.refresh();

        return view;
    }


}
