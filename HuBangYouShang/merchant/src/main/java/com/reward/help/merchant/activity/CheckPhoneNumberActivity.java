package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.StoreInfoResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.PersonalNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CheckPhoneNumberActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.btn_get_checkcode)
    Button mBtnGetCode;
    @BindView(R.id.tv_phonenumber_tip)
    EditText mEtPhoneNum;
    @BindView(R.id.password)
    EditText mEtCheckCode;

    String phone;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_check_phonenumber);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(getText(R.string.forget_password_get));
    }

    @OnClick({R.id.btn_next})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                //startActivity(new Intent(CheckPhoneNumberActivity.this,SetNewPasswordActivity.class));
                phone = mEtPhoneNum.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(CheckPhoneNumberActivity.this,"请输入手机号");
                    return;
                }

                String checkCode = mEtCheckCode.getText().toString();

                if (TextUtils.isEmpty(checkCode)) {
                    ToastUtils.show(CheckPhoneNumberActivity.this,"请输入验证码");
                    return;
                }

                checkCode(phone,checkCode);

                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                CheckPhoneNumberActivity.this.finish();
                break;
            case R.id.btn_get_checkcode:
                phone = mEtPhoneNum.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    getCheckCode(phone);
                } else {
                    ToastUtils.show(CheckPhoneNumberActivity.this,"请输入手机号");
                }
                break;
        }

    }

    private void checkCode(String phone, String checkCode) {
        MyProcessDialog.showDialog(CheckPhoneNumberActivity.this);
        subscribe = PersonalNetwork.getLoginApi().getCheckCode(phone,checkCode)
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
                            ToastUtils.show(CheckPhoneNumberActivity.this,"验证成功");
                            startActivityForResult(new Intent(CheckPhoneNumberActivity.this,SetNewPasswordActivity.class),1);
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    private void getCheckCode(String phone) {
        MyProcessDialog.showDialog(CheckPhoneNumberActivity.this);
        subscribe = PersonalNetwork.getLoginApi().getVerificationCodeBean(phone)
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            CheckPhoneNumberActivity.this.finish();
        }
    }
}
