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


public class SetNewPasswordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_set_new_password)
    EditText mEtPwd1;

    @BindView(R.id.et_set_confirm_password)
    EditText mEtPwd2;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);
        mTvTitle.setText(getText(R.string.forget_password_get));
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        if (TextUtils.isEmpty(mEtPwd1.getText().toString().trim())) {
            ToastUtils.show(mContext,"请输入新密码");
            return;
        }

        if (TextUtils.isEmpty(mEtPwd2.getText().toString().trim())) {
            ToastUtils.show(mContext,"请再次输入新密码");
            return;
        }

        MyProcessDialog.showDialog(SetNewPasswordActivity.this);
        subscribe = PersonalNetwork.getLoginApi().getUpdatePwd(mEtPwd1.getText().toString().trim(),mEtPwd2.getText().toString().trim())
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
                            ToastUtils.show(SetNewPasswordActivity.this,"修改成功");
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
