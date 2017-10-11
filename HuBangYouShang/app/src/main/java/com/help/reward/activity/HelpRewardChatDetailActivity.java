package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpRewardinfoCommentDetailAdapter;
import com.help.reward.bean.HelpRewardCommentBean;
import com.help.reward.bean.Response.HelpRewardChatDetailResponse;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.MyProcessDialog;
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
 * huoshang评论详情
 * Created by xfz on 2017/3/24.
 */

public class HelpRewardChatDetailActivity extends BaseActivity {
    protected Subscription subscribe;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;

    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.lv_helprewardchat)
    LRecyclerView lRecyclerview;
    private int numSize = 15;
    private HelpRewardinfoCommentDetailAdapter adapter;
    public List<HelpRewardCommentBean> mDatas = new ArrayList<>();
    String id, post_id;
    int curpage = 1;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    String hint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helprewardcommentdetail);
        ButterKnife.bind(this);
        id = getIntent().getExtras().getString("id");
        post_id = getIntent().getExtras().getString("post_id");
        hint = getIntent().getExtras().getString("hint");
        initView();
        initRecycler();

        if (StringUtils.checkStr(id)) {
            MyProcessDialog.showDialog(mContext);
            requestData();
        }

    }

    private void initView() {
        tvTitle.setText("详情页");
        tvTitleRight.setVisibility(View.GONE);
        et_comment.setHint(hint);
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelpRewardinfoCommentDetailAdapter(this);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        lRecyclerview.setVisibility(View.GONE);
        initRefreshListener();
        initLoadMoreListener();
    }


    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
                curpage = 1;
                requestData();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                requestData();
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_send})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_send:
                //发送跟帖
                String comment = et_comment.getText().toString().trim();
                if (StringUtils.checkStr(comment)) {
                    subComment(comment);
                } else {
                    ToastUtils.show(mContext, "请输入内容");
                }
                break;
        }
    }

    private void requestData() {
        subscribe = HelpNetwork
                .getHelpApi()
                .getRewardCommentDetailBean(App.APP_CLIENT_KEY, "reward_commment_expand", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpRewardChatDetailResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (curpage != 1) {
                            curpage--;
                        }
                    }

                    @Override
                    public void onNext(HelpRewardChatDetailResponse response) {
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            lRecyclerview.setVisibility(View.VISIBLE);
                            if (curpage == 1) {
                                adapter.setDataList(response.data);
                            } else {
                                adapter.addAll(response.data);
                            }
                            if (!response.hasmore) {
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                lRecyclerview.setLoadMoreEnabled(true);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void subComment(String content) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getSubRewardChatBean(App.APP_CLIENT_KEY, "private_chat", post_id, id, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            et_comment.setText("");
                            ToastUtils.show(mContext, "回复成功");
                            if (!StringUtils.checkStr(id)) {
                                setResult(RESULT_OK);
                                finish();
                                return;
                            }
                            MyProcessDialog.showDialog(mContext);
                            requestData();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
        ActivitySlideAnim.slideOutAnim(this);
    }

}
