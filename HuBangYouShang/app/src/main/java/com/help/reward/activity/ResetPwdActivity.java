package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置密码
 *
 * Created by wuxiaojun on 2017/1/15.
 */

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tv_title.setText(R.string.string_forget_pwd_title);
        tv_title_right.setVisibility(View.GONE);
    }

    private void initData() {
        initTimer();

    }

    private void initTimer() {

    }

    @OnClick({R.id.iv_title_back, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(ResetPwdActivity.this);

                break;
            case R.id.btn_commit: // 提交

                break;
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
