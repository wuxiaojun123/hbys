package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的账户
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_discount_amount)
    LinearLayout ll_discount_amount;// 优惠金额
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

    @OnClick({R.id.tv_balance,R.id.tv_account_help_reward,R.id.tv_coupon,R.id.tv_general_volume,R.id.ll_discount_amount,
            R.id.tv_balance_recharge, R.id.tv_coupon_trading, R.id.tv_coupon_generic, R.id.tv_exchange})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_balance: // 余额
                startActivity(new Intent(MyAccountActivity.this, MyBalanceActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_account_help_reward: // 帮赏分
                startActivity(new Intent(MyAccountActivity.this, MyAccountHelpRewardActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_coupon: // 优惠劵
                startActivity(new Intent(MyAccountActivity.this, MyCouponActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_general_volume: // 通用卷
                startActivity(new Intent(MyAccountActivity.this, MyGeneralVolumeActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.ll_discount_amount: // 优惠百分比
                startActivity(new Intent(MyAccountActivity.this, DiscountAmountActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_balance_recharge:
                // 余额充值
                startActivity(new Intent(MyAccountActivity.this, PrepaidBalanceActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);


                break;
            case R.id.tv_coupon_trading:
                // 优惠劵交易
                startActivity(new Intent(MyAccountActivity.this, MyCouponActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_coupon_generic:
                // 通用卷兑换
                startActivity(new Intent(MyAccountActivity.this, ExchangeOptionActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_exchange:
                // 帮赏分兑换


                break;
        }
    }


}
