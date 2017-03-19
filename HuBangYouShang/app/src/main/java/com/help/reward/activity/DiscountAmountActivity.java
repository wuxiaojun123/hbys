package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BalanceExchangeResponse;
import com.help.reward.bean.Response.DiscountAmountResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的账户--优惠金额
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class DiscountAmountActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_discount_percentage)
    TextView tv_discount_percentage; // 优惠百分比

    @BindView(R.id.tv_discount_level)
    TextView tv_discount_level; // 优惠等级

    @BindView(R.id.tv_content)
    TextView tv_content; // 优惠内容

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_amount);
        ButterKnife.bind(this);
        initNetwork();
    }

    private Subscription subscribe;

    private void initNetwork() {
        subscribe = PersonalNetwork
                .getResponseApi()
                .getDiscountAmountResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<DiscountAmountResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(DiscountAmountResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                tv_discount_percentage.setText(response.data.discount_level+"%");
                                tv_discount_level.setText(response.data.discount_level);
                                tv_content.setText(response.data.rule);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.tv_exchange})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                ActivitySlideAnim.slideOutAnim(DiscountAmountActivity.this);

                break;
            case R.id.tv_exchange:
                // 帮赏分兑换通用卷
                startActivity(new Intent(DiscountAmountActivity.this, GeneralExchangeVolumeActivity.class));
                ActivitySlideAnim.slideInAnim(DiscountAmountActivity.this);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
