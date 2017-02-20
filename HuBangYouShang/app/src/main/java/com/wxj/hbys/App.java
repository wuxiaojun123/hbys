package com.wxj.hbys;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * 更新一下
 * <p>
 * Created by wuxiaojun on 2017/1/4.
 */

public class App extends Application {

    public static final String A = "remove test git commit";

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);

    }


}
