package com.wxj.hbys.network.base;

import com.idotools.utils.JudgeNetWork;
import com.idotools.utils.ToastUtils;
import com.wxj.hbys.App;
import com.wxj.hbys.view.LoadingDialog;

import rx.Subscriber;
import rx.Subscription;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by wuxiaojun on 2017/2/28.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onStart() {
        super.onStart();
        if(!JudgeNetWork.isNetAvailable(App.getApplication())){
            ToastUtils.show(App.getApplication(),"请检查网络!");
            return;
        }
        // 显示进度条
    }

    @Override
    public void onCompleted() {
        // 关闭进度条
    }

    /*@Override
    public void onError(Throwable e) {
    }
    @Override
    public void onNext(T t) {

    }*/

}
