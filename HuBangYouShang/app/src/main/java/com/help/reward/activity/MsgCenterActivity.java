package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.MessageReadResponse;
import com.help.reward.network.MessageNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/17.
 * 消息中心
 */

public class MsgCenterActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.iv_msgcenter_post)
    ImageView ivMsgcenterPost;
    @BindView(R.id.layout_msgcenter_post)
    RelativeLayout layoutMsgcenterPost;
    @BindView(R.id.iv_msgcenter_deal)
    ImageView ivMsgcenterDeal;
    @BindView(R.id.layout_msgcenter_deal)
    RelativeLayout layoutMsgcenterDeal;
    @BindView(R.id.iv_msgcenter_complain)
    ImageView ivMsgcenterComplain;
    @BindView(R.id.layout_msgcenter_complain)
    RelativeLayout layoutMsgcenterComplain;
    @BindView(R.id.iv_msgcenter_sys)
    ImageView ivMsgcenterSys;
    @BindView(R.id.layout_msgcenter_ss)
    RelativeLayout layoutMsgcenterSs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgcenter);
        ButterKnife.bind(this);
        tvTitle.setText("消息中心");
        tvTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.layout_msgcenter_post, R.id.layout_msgcenter_deal,
            R.id.layout_msgcenter_complain, R.id.layout_msgcenter_ss})
    public void onCLick(View v) {
        Intent intent = new Intent(mContext, PostActivity.class);
        //type 0为私信、1为系统消息、2为留言、3帖子动态、4账户消息、5交易信息、6投诉消息
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.layout_msgcenter_post://帖子动态
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.layout_msgcenter_deal://交易信息
                intent.putExtra("type", "5");
                startActivity(intent);
                break;
            case R.id.layout_msgcenter_complain://投诉信息
                intent.putExtra("type", "6");
                startActivity(intent);
                break;
            case R.id.layout_msgcenter_ss://系统消息
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }

    /**
     * 请求网络
     */

    private void requestData() {

        subscribe = MessageNetwork
                .getMessageApi()
                .getMessageReadBean(App.APP_CLIENT_KEY, "new_msg_num")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MessageReadResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MessageReadResponse response) {
                        if (response.code == 200) {
                            /**
                             * "newsystem": "1",//系统消息
                             * "newpost": "2",帖子动态
                             * "trade": "0",交易消息
                             * "complain": "0"投诉消息
                             */
                            if (response.data.newsystem > 0) {
                                ivMsgcenterSys.setVisibility(View.VISIBLE);
                            } else {
                                ivMsgcenterSys.setVisibility(View.GONE);
                            }
                            if (response.data.newpost > 0) {
                                ivMsgcenterPost.setVisibility(View.VISIBLE);
                            } else {
                                ivMsgcenterPost.setVisibility(View.GONE);
                            }
                            if (response.data.trade > 0) {
                                ivMsgcenterDeal.setVisibility(View.VISIBLE);
                            } else {
                                ivMsgcenterDeal.setVisibility(View.GONE);
                            }
                            if (response.data.complain > 0) {
                                ivMsgcenterComplain.setVisibility(View.VISIBLE);
                            } else {
                                ivMsgcenterComplain.setVisibility(View.GONE);
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
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
