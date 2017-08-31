package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.pay.AlipayActivity;
import com.help.reward.activity.pay.WchatActivity;
import com.help.reward.bean.Response.PayTypeResponse;
import com.help.reward.bean.Response.PrepaidBalanceResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 充值余额
 * Created by wuxiaojun on 2017/2/13.
 */

public class PrepaidBalanceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_et_account)
    EditText id_et_account;

    @BindView(R.id.id_rl_wchat)
    RelativeLayout id_rl_wchat; // 微信支付
    @BindView(R.id.id_cb_wchat)
    CheckBox id_cb_wchat;

    @BindView(R.id.id_rl_alipay)
    RelativeLayout id_rl_alipay; // 支付宝支付
    @BindView(R.id.id_cb_alipay)
    CheckBox id_cb_alipay;

    @BindView(R.id.tv_ten)
    TextView tv_ten;
    @BindView(R.id.tv_twenty)
    TextView tv_twenty;
    @BindView(R.id.tv_fifty)
    TextView tv_fifty;
    @BindView(R.id.tv_one_hundred)
    TextView tv_one_hundred;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaid_balance);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        id_cb_wchat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    id_cb_alipay.setChecked(false);
                }
            }
        });
        id_cb_alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    id_cb_wchat.setChecked(false);
                }
            }
        });
    }

    private void initView() {
        tv_title.setText(R.string.string_chongzhi_yue_title);
        tv_title_right.setVisibility(View.GONE);
    }


    @OnClick({R.id.iv_title_back, R.id.id_rl_wchat, R.id.id_rl_alipay, R.id.id_btn_prepaid,
            R.id.tv_ten, R.id.tv_twenty, R.id.tv_fifty, R.id.tv_one_hundred})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(PrepaidBalanceActivity.this);

                break;
            case R.id.id_rl_wchat:

                break;
            case R.id.id_rl_alipay:

                break;
            case R.id.tv_ten:
                setDefaultTextBg();
                tv_ten.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv_selected);
                id_et_account.setText("10");

                break;
            case R.id.tv_twenty:
                setDefaultTextBg();
                tv_twenty.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv_selected);
                id_et_account.setText("20");

                break;
            case R.id.tv_fifty:
                setDefaultTextBg();
                tv_fifty.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv_selected);
                id_et_account.setText("50");

                break;
            case R.id.tv_one_hundred:
                setDefaultTextBg();
                tv_one_hundred.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv_selected);
                id_et_account.setText("100");

                break;
            case R.id.id_btn_prepaid:
                // 去充值
                String accountStr = id_et_account.getText().toString().trim();
                if (TextUtils.isEmpty(accountStr)) {
                    ToastUtils.show(mContext, "请输入金额");
                    return;
                }
                double accountParams = Double.parseDouble(accountStr);
                if (accountParams <= 0) {
                    ToastUtils.show(mContext, "金额必须大于0");
                    return;
                }
                if (!id_cb_wchat.isChecked() && !id_cb_alipay.isChecked()) {
                    ToastUtils.show(mContext, "请选择一种支付方式");
                    return;
                }
                requestGetPaysn(accountParams);

                break;
        }
    }


    private void setDefaultTextBg() {
        tv_ten.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv);
        tv_twenty.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv);
        tv_fifty.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv);
        tv_one_hundred.setBackgroundResource(R.drawable.shape_bg_prepaid_balance_tv);
    }

    private void requestGetPaysn(double accountParams) {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, "请登录");
            return;
        }
        String payType = null;
        if (id_cb_wchat.isChecked()) {
            payType = "wxpay";
        } else if (id_cb_alipay.isChecked()) {
            payType = "alipay";
        }
        ShopcartNetwork.getShopcartCookieApi()
                .getPrepaidBalanceResponse(accountParams + "", payType, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PrepaidBalanceResponse>() {
                    @Override
                    public void onNext(PrepaidBalanceResponse payTypeResponse) {
                        if (payTypeResponse.code == 200) {
                            String pay_sn = payTypeResponse.data.pay_sn;
                            // 580557337884748000
                            LogUtils.e("订单号是：" + pay_sn);
                            if (!TextUtils.isEmpty(pay_sn)) {
                                if (id_cb_wchat.isChecked()) {
                                    Intent mintent = new Intent(PrepaidBalanceActivity.this, WchatActivity.class);
                                    mintent.putExtra("pay_sn", pay_sn);
                                    startActivity(mintent);

                                } else if (id_cb_alipay.isChecked()) {
                                    Intent mintent = new Intent(PrepaidBalanceActivity.this, AlipayActivity.class);
                                    mintent.putExtra("pay_sn", pay_sn);
                                    startActivity(mintent);
                                }

                            }
                        } else {
                            ToastUtils.show(mContext, payTypeResponse.msg);
                        }
                    }
                });
    }

}
