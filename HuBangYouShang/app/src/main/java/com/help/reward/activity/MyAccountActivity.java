package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的账户
 * <p>
 * Created by wuxiaojun on 2017/1/10.
 */

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_photo)
    ImageView iv_photo; // 头像
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_subscibe)
    TextView tv_subscibe;

    @BindView(R.id.tv_available_predeposit)
    TextView tv_available_predeposit; // 余额
    @BindView(R.id.tv_reward_points)
    TextView tv_reward_points; // 帮赏分
    @BindView(R.id.tv_voucher)
    TextView tv_voucher; // 优惠劵
    @BindView(R.id.tv_general_voucher)
    TextView tv_general_voucher; // 通用劵
    @BindView(R.id.tv_discount_level)
    TextView tv_discount_level; // 帮赏分


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        if (App.mLoginReponse != null) {
            GlideUtils.loadCircleImage(App.mLoginReponse.avator, iv_photo);
            tv_title.setText(App.mLoginReponse.username);

            tv_available_predeposit.setText(App.mLoginReponse.available_predeposit);
            tv_reward_points.setText(App.mLoginReponse.point);
            tv_voucher.setText(App.mLoginReponse.voucher);
            tv_general_voucher.setText(App.mLoginReponse.general_voucher);
            tv_discount_level.setText(App.mLoginReponse.discount_level);

        } else {
            iv_photo.setImageResource(R.mipmap.img_my_default_photo);
            tv_title.setText(null);

            tv_available_predeposit.setText("0");
            tv_reward_points.setText("0");
            tv_voucher.setText("0");
            tv_general_voucher.setText("0");
            tv_discount_level.setText("0");
        }
    }

    @OnClick({R.id.id_back, R.id.tv_balance, R.id.tv_account_help_reward, R.id.tv_coupon, R.id.tv_general_volume,
            R.id.ll_discount_amount, R.id.tv_balance_recharge, R.id.tv_coupon_trading, R.id.tv_coupon_generic,
            R.id.tv_exchange})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
                // 退出
                finish();
                ActivitySlideAnim.slideOutAnim(MyAccountActivity.this);

                break;
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
            case R.id.ll_discount_amount: // 优惠金额
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
                startActivity(new Intent(MyAccountActivity.this, CouponTradingActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_coupon_generic:
                // 通用卷兑换
                startActivity(new Intent(MyAccountActivity.this, ExchangeOptionActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
            case R.id.tv_exchange:
                // 帮赏分兑换----跳转到余额兑换帮赏分界面
                startActivity(new Intent(MyAccountActivity.this, BalanceExchangeHelpScoreActivity.class));
                ActivitySlideAnim.slideInAnim(MyAccountActivity.this);

                break;
        }
    }


}
