package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.bean.Response.ComplaintStatusResponse;
import com.help.reward.manager.GotoHelpVoteInfoUtils;
import com.help.reward.network.HelpNetwork;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.MyVoteAdapter;
import com.help.reward.bean.Response.MyVoteResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的投票
 * Created by wuxiaojun on 2017/2/11.
 */
public class MyVoteActivity extends BaseActivity implements View.OnClickListener, MyVoteAdapter.OnMyVoteClickListener {

    private int numSize = 15;
    private int currentPage = 1;

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private MyVoteAdapter mHelpPostAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vote);
        ButterKnife.bind(this);
        initView();
        initNet();
        initRecyclerView();
        initEvent();
    }

    private void initEvent() {
        mHelpPostAdapter.setOnMyVoteClickListener(this);
    }

    private void initNet() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
        //  ?act=member_vote&op=list
        PersonalNetwork
                .getResponseApi()
                .getMyVoteResponse("member_vote", "list", currentPage + "", App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyVoteResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyVoteResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (currentPage == 1) {
                                    mHelpPostAdapter.setDataList(response.data);
                                } else {
                                    mHelpPostAdapter.addAll(response.data);
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

    private void initView() {
        tv_title.setText(R.string.string_my_vote_title);
        tv_title_right.setVisibility(View.GONE);
    }

    private GotoHelpVoteInfoUtils gotoHelpVoteInfoUtils;

    @Override
    public void onItemClick(int position) {
        if (gotoHelpVoteInfoUtils == null) {
            gotoHelpVoteInfoUtils = new GotoHelpVoteInfoUtils(MyVoteActivity.this);
        }
        gotoHelpVoteInfoUtils.gotoHelpVoteInfo(mHelpPostAdapter.getDataList().get(position).id);
    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(MyVoteActivity.this);

                break;
        }
    }

    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mHelpPostAdapter = new MyVoteAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHelpPostAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        initRefreshListener();
        initLoadMoreListener();
        initOnItemClickListener();
    }

    private void initOnItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, HelpVoteInfoActivity.class);
                intent.putExtra("id", mHelpPostAdapter.getDataList().get(position).post_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(MyVoteActivity.this);
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNet();
            }
        });
    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                initNet();
            }
        });
    }


}
