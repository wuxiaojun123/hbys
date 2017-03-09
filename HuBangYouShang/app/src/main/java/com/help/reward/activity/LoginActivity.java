package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.help.reward.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登陆
 * Created by wuxiaojun on 2017/1/6.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_login_phone_number)
    EditText et_login_phone_number;// 用户名
    @BindView(R.id.et_login_pwd)
    EditText et_login_pwd; // 密码


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_login, R.id.tv_register,R.id.tv_forget_pwd, R.id.tv_wetchat, R.id.tv_qq, R.id.tv_weibo})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();

                break;
            case R.id.tv_register:
                // 快速注册

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                ActivitySlideAnim.slideInAnim(LoginActivity.this);

                break;
            case R.id.tv_forget_pwd: // 忘记密码

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                ActivitySlideAnim.slideInAnim(LoginActivity.this);

                break;
            case R.id.tv_wetchat:

                break;
            case R.id.tv_qq:

                break;
            case R.id.tv_weibo:

                break;
        }
    }

    private Subscription subscribe;

    /***
     * 登录逻辑
     */
    private void login() {
        String username = et_login_phone_number.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(mContext, "请输入用户名");
            return;
        }
        String password = et_login_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(mContext, "请输入密码");
            return;
        }

        MyProcessDialog.showDialog(mContext);
        subscribe = PersonalNetwork
                .getLoginApi()
                .getLoginBean(username, password, Constant.PLATFORM_CLIENT).subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<LoginResponse>() {
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
                    public void onNext(LoginResponse res) {
                        MyProcessDialog.closeDialog();
                        if (res.code == 200) {
                            LogUtils.e("请求到的key是：" + res.data.key + "=======" + res.data.userid);
                            App.APP_CLIENT_KEY = res.data.key;
                            RxBus.getDefault().post("loginSuccess");

                            finish();
                            ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
