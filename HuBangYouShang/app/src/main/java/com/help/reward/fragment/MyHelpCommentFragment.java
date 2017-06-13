package com.help.reward.fragment;

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
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyHelpCommentAdapter;
import com.help.reward.bean.Response.MyHelpCommentResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyHelpCommentFragment extends BaseFragment {

    private int numSize = 15;
    private int currentPage = 1;

    private MyHelpCommentAdapter myHelpCommentAdapter;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;


    /*@Override
    protected int getLayoutId() {
        return R.layout.fragment_my_help_comment;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_help_post,container,false);
        ButterKnife.bind(this,view);
        init();

        return view;
    }

    @Override
    protected void init() {
        initRecyclerView();
        initNetwork();
    }

    private void initNetwork() {
        if(App.APP_CLIENT_KEY == null){
            return;
        }
        // ?act=member_index&op=my_seek_help
        PersonalNetwork
                .getResponseApi()
                .getMyHelpCommentResponse("member_index","my_seek_help",currentPage+"","comment", App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyHelpCommentResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
//                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyHelpCommentResponse response) {
//                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            LogUtils.e("获取数据成功。。。。"+response.data.size());
                            if (response.data != null) {
                                myHelpCommentAdapter.addAll(response.data);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);
                            lRecyclerview.setLoadMoreEnabled(false);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        myHelpCommentAdapter = new MyHelpCommentAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(myHelpCommentAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

}
