package com.wxj.hbys.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wxj.hbys.R;
import com.wxj.hbys.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public void onBackPressed() {
        ActivitySlideAnim.slideOutAnim(this);
        super.onBackPressed();
    }
}
