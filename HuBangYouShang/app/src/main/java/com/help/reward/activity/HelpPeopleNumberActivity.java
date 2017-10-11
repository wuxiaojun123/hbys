package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpPeopleNumberAdapter;
import com.help.reward.adapter.MessageAdapter;
import com.help.reward.bean.MessageBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.DeleteMessageResponse;
import com.help.reward.bean.Response.HelpPeopleNumberResponse;
import com.help.reward.bean.Response.MessageResponse;
import com.help.reward.bean.Response.MyBalanceResponse;
import com.help.reward.manager.GotoHelpVoteInfoUtils;
import com.help.reward.network.MessageNetwork;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.MyAccountHelpRewardRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 帮助人数
 */

public class HelpPeopleNumberActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;


    @BindView(R.id.rv_post)
    LRecyclerView lRecyclerview;
    @BindView(R.id.tv_number)
    TextView tv_number;

    private int numSize = 15;
    private HelpPeopleNumberAdapter adapter;
    List<HelpPeopleNumberResponse.HelpPeopleNumberBean> mDatas = new ArrayList<>();
    int currentPage = 1;
    String number = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_people_number);
        ButterKnife.bind(this);

        initView();
        initRecycler();
    }

    private void initView() {
        tvTitle.setText("帮助人数");
        tvTitleRight.setVisibility(View.GONE);
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelpPeopleNumberAdapter(mContext);
        LRecyclerViewAdapter ladapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(ladapter);

        lRecyclerview.setPullRefreshEnabled(true);
        lRecyclerview.setLoadMoreEnabled(true);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initRefreshListener();
        initLoadMoreListener();
        requestData();
    }


    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
                currentPage = 1;
                requestData();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                currentPage++;
                requestData();
            }
        });
    }


    @OnClick({R.id.iv_title_back})
    public void onCLick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(HelpPeopleNumberActivity.this);

                break;
        }
    }

    /**
     * 请求网络
     */

    private void requestData() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, R.string.string_please_login);
            return;
        }
        subscribe = PersonalNetwork
                .getResponseApi()
                .getHelpPeopleNumberResponse("member_index", "myhelppeople", "" + currentPage, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpPeopleNumberResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpPeopleNumberResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                number = response.data.helppeople;
                                tv_number.setText("您已提供帮助人数:" + number);
                                adapter.number = number;
                                adapter.addAll(response.data.list);
                            }
                            if (!response.hasmore) { // 是否有更多数据
                                lRecyclerview.setNoMore(true);
//                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                currentPage += 1;
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private Subscription subscribe;

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
