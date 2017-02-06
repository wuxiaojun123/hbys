package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wxj.hbys.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.tv_help_center, R.id.tv_feedback, R.id.tv_about})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_help_center:
                startActivity(new Intent(SettingActivity.this, HelpCenterActivity.class));

                break;
            case R.id.tv_feedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));

                break;
            case R.id.tv_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));

                break;
        }
    }


}
