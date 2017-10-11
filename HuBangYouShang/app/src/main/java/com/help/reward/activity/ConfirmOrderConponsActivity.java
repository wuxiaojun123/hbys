package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.help.reward.R;

import butterknife.ButterKnife;

/**
 * Created by ADBrian on 16/04/2017.
 */

public class ConfirmOrderConponsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_order_conpons);
        ButterKnife.bind(this);
    }
}
