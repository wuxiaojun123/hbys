package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.Response.VerificationCodeResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.CountDownTimeUtils;
import com.help.reward.utils.SmsSDKUtils;
import com.help.reward.utils.ValidateUtil;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 忘记密码
 * <p>
 * Created by wuxiaojun on 2017/1/15.
 */

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_code)
    EditText et_code;

    private CountDownTimeUtils mTimer;
    public String verificationCode; // 请求到的code

    private SmsSDKUtils smsSDKUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        } else {
            tv_title.setText(R.string.string_identity_title);
        }
        tv_title_right.setVisibility(View.GONE);
    }

    private void initData() {
        initTimer();
        smsSDKUtils = new SmsSDKUtils(mContext);
        smsSDKUtils.setOnSMSSDKListener(new SmsSDKUtils.OnSMSSDKListener() {
            @Override
            public void onSMSSDKSendSuccessListener() {
                tv_code.setClickable(false);
                mTimer.start();
            }
        });
    }

    private void initTimer() {
        mTimer = new CountDownTimeUtils(CountDownTimeUtils.millisInFuture, CountDownTimeUtils.countDownInterval);
        mTimer.setOnCountDownTimeListener(new CountDownTimeUtils.OnCountDownTimeListener() {
            @Override
            public void onTick(long millisUntilFinished) { // 计时开始
                tv_code.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() { // 计时结束
                tv_code.setClickable(true);
                tv_code.setText(R.string.string_recapture);
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_code, R.id.btn_next})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(ForgetPwdActivity.this);

                break;
            case R.id.tv_code: // 点击验证code
                String registerUserName = et_phone_number.getText().toString().trim();
                if (!TextUtils.isEmpty(registerUserName)) {
                    LogUtils.e("点击获取code");
                    smsSDKUtils.sendCode(registerUserName);
                } else {
                    ToastUtils.show(mContext, "请输入手机号");
                }

                break;
            case R.id.btn_next: // 需要验证验证码，然后再下一步
                startActivity(new Intent(ForgetPwdActivity.this, ResetPwdActivity.class));
                ActivitySlideAnim.slideInAnim(ForgetPwdActivity.this);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (smsSDKUtils != null) {
            smsSDKUtils.destroySms();
        }
        super.onDestroy();
    }

}
