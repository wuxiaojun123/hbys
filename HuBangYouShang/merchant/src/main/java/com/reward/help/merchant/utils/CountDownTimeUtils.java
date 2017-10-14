package com.reward.help.merchant.utils;

import android.os.CountDownTimer;

/**
 * 计时器
 * Created by wuxiaojun on 17-3-9.
 */

public class CountDownTimeUtils extends CountDownTimer {

    public static final long countDownInterval = 1000; // 每隔多久
    public static final long millisInFuture = 60 * countDownInterval;  // 总共隔多久

    public CountDownTimeUtils(long millisInFuture,long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(onCountDownTimeListener != null){
            onCountDownTimeListener.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if(onCountDownTimeListener != null){
            onCountDownTimeListener.onFinish();
        }
    }

    private OnCountDownTimeListener onCountDownTimeListener;

    public void setOnCountDownTimeListener(OnCountDownTimeListener listener){
        this.onCountDownTimeListener = listener;
    }

    public interface OnCountDownTimeListener{
        void onTick(long millisUntilFinished);
        void onFinish();
    }

}
