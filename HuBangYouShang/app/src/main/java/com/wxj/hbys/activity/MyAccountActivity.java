package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wxj.hbys.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 我的账户
 *
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_balance_recharge)
    TextView tv_balance_recharge;//余额充值
    @BindView(R.id.tv_coupon_trading)
    TextView tv_coupon_trading;//优惠劵交易
    @BindView(R.id.tv_coupon_generic)
    TextView tv_coupon_generic;//通用卷交易
    @BindView(R.id.tv_exchange)
    TextView tv_exchange;//帮赏分兑换


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_balance_recharge, R.id.tv_coupon_trading, R.id.tv_coupon_generic, R.id.tv_exchange})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_balance_recharge:
                // 余额充值

                break;
            case R.id.tv_coupon_trading:
                // 优惠劵交易

                break;
            case R.id.tv_coupon_generic:
                // 通用卷交易

                break;
            case R.id.tv_exchange:
                // 帮赏分兑换
                startActivity(new Intent(MyAccountActivity.this,MyAccountHelpRewardActivity.class));

                break;
        }
    }


}
