package com.help.reward.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.help.reward.R;

/**
 * Created by MXY on 2017/2/26.
 */

public class ShopInfoNewFragment extends BaseFragment{

    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_shopinfo_new, null);
        }
        return contentView;
    }
}
