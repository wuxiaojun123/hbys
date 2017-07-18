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
import com.help.reward.adapter.MessageAdapter;
import com.help.reward.bean.MessageBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.ComplaintStatusResponse;
import com.help.reward.bean.Response.DeleteMessageResponse;
import com.help.reward.bean.Response.MessageResponse;
import com.help.reward.manager.GotoHelpVoteInfoUtils;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.MessageNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
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
 * 帖子动态
 */

public class PostActivity extends BaseActivity implements MessageAdapter.IonSlidingViewClickListener {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.rv_post)
    LRecyclerView lRecyclerview;
    private int numSize = 15;
    private MessageAdapter adapter;
    List<MessageBean> mDatas = new ArrayList<>();
    int curpage = 1;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        type = getIntent().getExtras().getString("type");
        tvTitleRight.setText("清空");
        if ("3".equals(type)) {
            tvTitle.setText("帖子动态");
        } else if ("5".equals(type)) {
            tvTitle.setText("交易信息");
            tvTitleRight.setVisibility(View.GONE);
        } else if ("6".equals(type)) {
            tvTitle.setText("投诉信息");
        } else if ("1".equals(type)) {
            tvTitle.setText("系统消息");
        }else if ("4".equals(type)) {
            tvTitle.setText("账户消息");
        }

        initRecycler();

    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this, type);
        adapter.setDatas(mDatas);
        LRecyclerViewAdapter ladapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(ladapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initRefreshListener();
        initLoadMoreListener();
        MyProcessDialog.showDialog(mContext);
        requestData();
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


    @OnClick({R.id.iv_title_back, R.id.tv_title_right})
    public void onCLick(View view) {

        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(PostActivity.this);

                break;
            case R.id.tv_title_right:
                clearMessage();
                break;
        }
    }

    private GotoHelpVoteInfoUtils gotoHelpVoteInfoUtils;

    @Override
    public void onItemClick(View view, int position) {
        position=position-1;
        if ("3".equals(type)) {
            Intent intent = null;
            if("help".equalsIgnoreCase(adapter.getDataList().get(position).noteString)){
                intent = new Intent(mContext, HelpSeekInfoActivity.class);
            }else{
                intent = new Intent(mContext, HelpRewardInfoActivity.class);
            }
            intent.putExtra("id", adapter.getDataList().get(position).related_id);
            startActivity(intent);
            ActivitySlideAnim.slideInAnim(this);
        } else if ("5".equals(type)) {
            Intent intent = new Intent(mContext, OrderDetailsActivity.class);
            intent.putExtra("orderid", adapter.getDataList().get(position).noteObject.order_sn);
            startActivity(intent);
            ActivitySlideAnim.slideInAnim(this);
        } else if ("6".equals(type)) {
            if (gotoHelpVoteInfoUtils == null) {
                gotoHelpVoteInfoUtils = new GotoHelpVoteInfoUtils(PostActivity.this);
            }
            gotoHelpVoteInfoUtils.gotoHelpVoteInfo(adapter.getDataList().get(position).related_id);

        } else if ("1".equals(type)) {

        }

    }

    @Override
    public void onDeleteBtnCilck(View view, final int position) {
        MessageBean bean = null;
        if(position>=1||position<=adapter.getDataList().size()){
            bean=adapter.getDataList().get(position-1);
        }
        if (bean != null) {
            MyProcessDialog.showDialog(mContext);
            MessageNetwork
                    .getMessageApi()
                    .deleteMessageBean(App.APP_CLIENT_KEY, bean.message_id, "dropcommonmsg")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<DeleteMessageResponse>() {
                        @Override
                        public void onError(Throwable e) {
                            MyProcessDialog.closeDialog();
                            e.printStackTrace();
                            ToastUtils.show(mContext, R.string.string_error);
                        }

                        @Override
                        public void onNext(DeleteMessageResponse response) {
                            MyProcessDialog.closeDialog();
                            if (response.code == 200) { // 删除成功
                                adapter.removeData(position);
                                ToastUtils.show(mContext, response.data);
                            } else {
                                ToastUtils.show(mContext, response.msg);
                            }
                        }
                    });

        }

    }

    /**
     * 请求网络
     */

    private void requestData() {

        subscribe = MessageNetwork
                .getMessageApi()
                .getMessageBean(App.APP_CLIENT_KEY, type, "message", curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MessageResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1) {
                            curpage--;
                        }
                    }


                    @Override
                    public void onNext(MessageResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    adapter.setDatas(response.data.list);
                                    if (adapter.getDataList().size() == 0) {
                                        ToastUtils.show(mContext, "暂无消息");
                                    }
                                } else {
                                    adapter.addAll(response.data.list);
                                }
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

    public void clearMessage() {
        MyProcessDialog.showDialog(mContext);
        MessageNetwork
                .getMessageApi()
                .clearMessageBean(App.APP_CLIENT_KEY, type, "clear_up")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) { // 删除成功
                            adapter.setDatas(null);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /*public void getComplaintStatus(final String id) {
        MyProcessDialog.showDialog(mContext);
        HelpNetwork
                .getHelpApi()
                .getComplaintStatusBean(App.APP_CLIENT_KEY, "complaint_status",id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ComplaintStatusResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ComplaintStatusResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if("待申诉".equalsIgnoreCase(response.data.status)||"已申诉".equalsIgnoreCase(response.data.status)){
                                Intent intent = new Intent(mContext, HelpComplainedDetailActivity.class);
                                intent.putExtra("complaint_id", id);
                                startActivity(intent);

                            }else{
                                Intent intent = new Intent(mContext, HelpVoteInfoActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                            ActivitySlideAnim.slideInAnim(PostActivity.this);

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }*/


    private Subscription subscribe;

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
