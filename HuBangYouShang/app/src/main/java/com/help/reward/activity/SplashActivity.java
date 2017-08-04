package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

/**
 * Created by wuxiaojun on 2017/1/4.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(1,2000);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent mIntent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(mIntent);
            finish();
            ActivitySlideAnim.slideInAnim(SplashActivity.this);
        }
    };

}
