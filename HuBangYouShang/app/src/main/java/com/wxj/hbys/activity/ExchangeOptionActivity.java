package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wxj.hbys.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择兑换方式
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */
public class ExchangeOptionActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_option);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_balance_exchange, R.id.tv_bonus_points})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_balance_exchange:
                // 余额兑换通用卷
                startActivity(new Intent(ExchangeOptionActivity.this, BalanceExchangeHelpScoreActivity.class));

                break;
            case R.id.tv_bonus_points:
                // 帮赏分兑换通用卷
                startActivity(new Intent(ExchangeOptionActivity.this, GeneralExchangeVolumeActivity.class));

                break;
        }
    }


}
