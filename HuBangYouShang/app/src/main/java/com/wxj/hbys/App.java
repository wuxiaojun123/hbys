package com.wxj.hbys;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * 更新一下
 * <p>
 * Created by wuxiaojun on 2017/1/4.
 */

public class App extends Application {

    private static App mApp;
    public static String APP_CLIENT_KEY = null;

    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
        mApp = this;
    }

    public static App getApplication(){
        return mApp;
    }

}
