package com.help.reward.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.DoAnalyticsManager;
import com.help.reward.utils.StatusBarUtil;

/**
 * Created by wuxiaojun on 2017/1/4.
 */

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), StatusBarUtil.DEFAULT_ALPHA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoAnalyticsManager.pageResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DoAnalyticsManager.pagePause(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        ActivitySlideAnim.slideOutAnim(this);
    }
}
