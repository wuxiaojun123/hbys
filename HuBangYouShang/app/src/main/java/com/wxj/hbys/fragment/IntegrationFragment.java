package com.wxj.hbys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxj.hbys.R;

/**
 * Created by wuxiaojun on 2017/1/8.
 */

public class IntegrationFragment extends BaseFragment {

    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(contentView == null){
            contentView = inflater.inflate(R.layout.fragment_integration,null);
        }

        return contentView;
    }


}
