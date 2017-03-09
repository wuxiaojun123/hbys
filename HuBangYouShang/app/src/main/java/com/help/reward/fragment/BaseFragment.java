package com.help.reward.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by wuxiaojun on 2017/1/8.
 */

public class BaseFragment extends Fragment {

    protected Context mContext;
    protected Subscription subscribe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this,view);
        init();

        return view;
    }

    protected void init(){

    }

    protected int getLayoutId(){
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscribe != null && !subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
    }
}
