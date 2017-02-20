package com.wxj.hbys.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxj.hbys.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布求助帖
 * Created by MXY on 2017/2/19.
 */

public class ReleaseHelpActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_release_help_title)
    EditText etReleaseHelpTitle;
    @BindView(R.id.tv_release_help_address)
    TextView tvReleaseHelpAddress;
    @BindView(R.id.tv_release_help_type)
    TextView tvReleaseHelpType;
    @BindView(R.id.tv_release_help_data)
    TextView tvReleaseHelpData;
    @BindView(R.id.tv_release_help_score)
    TextView tvReleaseHelpScore;
    @BindView(R.id.et_release_help_content)
    EditText etReleaseHelpContent;
    @BindView(R.id.iv_release_addphoto)
    ImageView ivReleaseAddphoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_help);
        ButterKnife.bind(this);

        initView();
    }
    private void initView() {
        tvTitle.setText("发布求助帖");
        tvTitleRight.setText("发布");
    }

    @OnClick({R.id.iv_title_back,R.id.tv_release_help_address,R.id.tv_release_help_type,
              R.id.tv_release_help_data,R.id.tv_release_help_score,R.id.iv_release_addphoto})
    void click(View v){
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_release_help_address:
                break;
            case R.id.tv_release_help_type:
                break;
            case R.id.tv_release_help_data:
                break;
            case R.id.tv_release_help_score:
                break;
            case R.id.iv_release_addphoto:
                break;
        }
    }
}
