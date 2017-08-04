package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.help.reward.R;
import com.help.reward.utils.SharedPreferenceConstant;
import com.idotools.utils.SharedPreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuxiaojun on 2017/8/4.
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.banner_guide)
    ConvenientBanner convenientBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        boolean isFirst = SharedPreferencesHelper.getInstance(mContext).getBoolean(SharedPreferenceConstant.KEY_IS_FIRST, false);
        if(isFirst){

        }
    }


}
