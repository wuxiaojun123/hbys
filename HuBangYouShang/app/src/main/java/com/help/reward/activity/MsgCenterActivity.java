package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick({R.id.iv_title_back,R.id.layout_msgcenter_post,R.id.layout_msgcenter_deal,
              R.id.layout_msgcenter_complain,R.id.layout_msgcenter_ss})
    public void onCLick(View v){
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.layout_msgcenter_post://帖子动态

                startActivity(new Intent(mContext, PostActivity.class));
                break;
            case R.id.layout_msgcenter_deal://交易信息
                startActivity(new Intent(mContext, TradeActivity.class));
                break;
            case R.id.layout_msgcenter_complain://投诉信息

                break;
            case R.id.layout_msgcenter_ss://系统消息

                break;
        }
    }
}
