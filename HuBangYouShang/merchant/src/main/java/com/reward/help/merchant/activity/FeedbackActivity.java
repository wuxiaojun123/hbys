package com.reward.help.merchant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reward.help.merchant.R;
import com.reward.help.merchant.chat.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 *
 * Created by wuxiaojun on 2017/1/8.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tv_title.setText("意见反馈");
    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_title_back:
                finish();
                //ActivitySlideAnim.slideOutAnim(FeedbackActivity.this);

                break;
        }
    }


}
