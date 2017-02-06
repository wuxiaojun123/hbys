package com.wxj.hbys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxj.hbys.R;

import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 2017/1/15.
 */

public class MyAccountHelpRewardFragment extends BaseFragment {

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_my_account_help_reward,null);
        }
        ButterKnife.bind(this,view);
        return view;
    }


}
