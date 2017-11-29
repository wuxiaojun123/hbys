package com.reward.help.merchant.utils;

import android.app.Activity;
import android.content.Context;

import com.reward.help.merchant.App;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by wuxiaojun on 16-11-7.
 */
public class DoAnalyticsManager {

    public static void pageResume(Activity activity) {
//        MobclickAgent.onPageStart(activity.getClass().getSimpleName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(activity);          //统计时长
    }

    public static void pagePause(Activity activity) {
//        MobclickAgent.onPageEnd(activity.getClass().getSimpleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(activity);
    }


    public static void pageFragmentResume(String name, Context context) {
        MobclickAgent.onPageStart(name); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(context);
    }

    public static void pageFragmentPause(String name, Context context) {
        MobclickAgent.onPageEnd(name); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(context);
    }

    public static void event(String eventId) {
        MobclickAgent.onEvent(App.getApplication(), eventId);
    }

    public static void event(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void event(Context context, String eventId, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, eventId, map);
    }

}
