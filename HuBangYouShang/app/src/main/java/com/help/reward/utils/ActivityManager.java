package com.help.reward.utils;

import com.help.reward.activity.BaseActivity;

import java.util.Stack;

/**
 * Created by wuxiaojun on 17-8-7.
 */

public class ActivityManager {

    private static Stack<BaseActivity> activityStack;
    private static ActivityManager instance;

    public static ActivityManager getActivityManager() {
        if (instance == null) {
            instance = new ActivityManager();
            activityStack = new Stack<>();
        }
        return instance;
    }

    public void addActivity(BaseActivity activity) {
        activityStack.add(activity);
    }


    public BaseActivity getCurrentActivity() {
        BaseActivity activity = activityStack.lastElement();
        return activity;
    }

    public void removeActivity(BaseActivity activity) {
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (activityStack.get(i) != null) {
                activityStack.get(i).finish();
            }
        }
    }


}
