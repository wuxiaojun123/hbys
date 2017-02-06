package com.wxj.hbys;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by wuxiaojun on 2017/1/4.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);

    }


}
