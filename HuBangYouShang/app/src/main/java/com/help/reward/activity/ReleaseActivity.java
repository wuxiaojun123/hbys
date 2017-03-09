package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布帖子
 * Created by MXY on 2017/2/19.
 */

public class ReleaseActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_release_help)
    TextView tvReleaseHelp;
    @BindView(R.id.tv_release_rewards)
    TextView tvReleaseRewards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("发布帖子");
        tvTitleRight.setVisibility(View.GONE);
    }
    @OnClick({R.id.iv_title_back,R.id.tv_release_help,R.id.tv_release_rewards})
    void onCLick(View v){
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_release_help:
                startActivity(new Intent(mContext,ReleaseHelpActivity.class));
                break;
            case R.id.tv_release_rewards:
                startActivity(new Intent(mContext,ReleaseRewardActivity.class));
                break;
        }
    }
}
