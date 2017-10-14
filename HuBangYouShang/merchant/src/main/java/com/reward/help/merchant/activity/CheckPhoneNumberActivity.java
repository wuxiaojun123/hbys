package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.PersonalNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.utils.CountDownTimeUtils;
import com.reward.help.merchant.utils.SmsSDKUtils;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/****
 * 找回密码
 */
public class CheckPhoneNumberActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.username)
    EditText mEtPhoneNum;
    @BindView(R.id.password)
    EditText mEtCheckCode;

    String phone;

    private CountDownTimeUtils mTimer;
    private SmsSDKUtils smsSDKUtils;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_check_phonenumber);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        } else {
            mTvTitle.setText(getText(R.string.forget_password_get));
        }
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

    @OnClick({R.id.iv_title_back, R.id.btn_next, R.id.tv_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                //startActivity(new Intent(CheckPhoneNumberActivity.this,SetNewPasswordActivity.class));
                phone = mEtPhoneNum.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(CheckPhoneNumberActivity.this, "请输入手机号");
                    return;
                }
                String checkCode = mEtCheckCode.getText().toString();
                if (TextUtils.isEmpty(checkCode)) {
                    ToastUtils.show(CheckPhoneNumberActivity.this, "请输入验证码");
                    return;
                }
                checkCode(phone, checkCode);

                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                CheckPhoneNumberActivity.this.finish();
                break;
            case R.id.tv_code:
                phone = mEtPhoneNum.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    LogUtils.e("点击获取code");
                    smsSDKUtils.sendCode(phone);
                } else {
                    ToastUtils.show(mContext, "请输入手机号");
                }
                break;
        }
    }

    private void checkCode(final String phone, String checkCode) {
        MyProcessDialog.showDialog(CheckPhoneNumberActivity.this);
        subscribe = PersonalNetwork.getLoginApi().getCheckCaptchaBean(phone, checkCode)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(CheckPhoneNumberActivity.this, "验证成功");
                            Intent mIntent = new Intent(CheckPhoneNumberActivity.this, SetNewPasswordActivity.class);
                            mIntent.putExtra("phone", phone);
                            startActivityForResult(mIntent, 1);
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    /*private void getCheckCode(String phone) {
        MyProcessDialog.showDialog(CheckPhoneNumberActivity.this);
        subscribe = PersonalNetwork.getLoginApi().getVerificationCodeBean(phone,"3")
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(CheckPhoneNumberActivity.this,"获取验证码成功");
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            CheckPhoneNumberActivity.this.finish();
        }
    }
}
