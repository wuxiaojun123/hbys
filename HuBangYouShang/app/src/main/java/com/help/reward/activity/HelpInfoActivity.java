package com.help.reward.activity;

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
 * Created by MXY on 2017/2/20.
 */

public class HelpInfoActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.iv_helpinfo_headimg)
    ImageView ivHelpinfoHeadimg;
    @BindView(R.id.tv_helpinfo_uname)
    TextView tvHelpinfoUname;
    @BindView(R.id.tv_helpinfo_date)
    TextView tvHelpinfoDate;
    @BindView(R.id.tv_helpinfo_count)
    TextView tvHelpinfoCount;
    @BindView(R.id.tv_helpinfo_title)
    TextView tvHelpinfoTitle;
    @BindView(R.id.tv_helpinfo_content)
    TextView tvHelpinfoContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpinfo);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("详情");
        tvTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back})
    void click(View v){
        switch (v.getId()){
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
