package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.BaseResponse;
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

/***
 * 设置新密码
 */
public class SetNewPasswordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_set_new_password)
    EditText mEtPwd1;

    @BindView(R.id.et_set_confirm_password)
    EditText mEtPwd2;

    private String phone;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        initView();
        phone = getIntent().getStringExtra("phone");
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(getText(R.string.forget_password_get));
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                //startActivity(new Intent(SetNewPasswordActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                sendNewPwd();
                break;
            case R.id.iv_title_back:
                hideSoftKeyboard();
                SetNewPasswordActivity.this.finish();
                break;
        }

    }

    private void sendNewPwd() {
        String pwd = mEtPwd1.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(mContext, "请输入新密码");
            return;
        }
        String pwd1 = mEtPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(pwd1)) {
            ToastUtils.show(mContext, "请再次输入新密码");
            return;
        }
        if (!TextUtils.isEmpty(pwd1)) {
            if (pwd.equals(pwd1)) {
                judge(pwd, pwd1);
            } else {
                ToastUtils.show(mContext, "请检查您的确认密码");
            }
        } else {
            ToastUtils.show(mContext, "请输入确认密码");
        }
    }

    private void judge(String pwd, String pwd1) {
        MyProcessDialog.showDialog(SetNewPasswordActivity.this);
        subscribe = PersonalNetwork.getLoginApi().getResetPasswordBean(pwd, pwd1,phone)
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
                            ToastUtils.show(SetNewPasswordActivity.this, "修改成功");
                            setResult(RESULT_OK);
                            finish();
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

}
