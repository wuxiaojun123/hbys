package com.help.reward.activity;

import android.content.Intent;
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
 * 设置
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_setting_title);

    }


    @OnClick({R.id.iv_title_back,R.id.tv_help_center, R.id.tv_feedback, R.id.tv_about})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(SettingActivity.this);

                break;
            case R.id.tv_help_center:
                startActivity(new Intent(SettingActivity.this, HelpCenterActivity.class));
                ActivitySlideAnim.slideInAnim(SettingActivity.this);

                break;
            case R.id.tv_feedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                ActivitySlideAnim.slideInAnim(SettingActivity.this);

                break;
            case R.id.tv_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                ActivitySlideAnim.slideInAnim(SettingActivity.this);

                break;
        }
    }


}
