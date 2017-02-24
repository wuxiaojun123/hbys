package com.wxj.hbys.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.wxj.hbys.R;
import com.wxj.hbys.bean.Response.LoginResponse;
import com.wxj.hbys.network.LoginRegisterNetwork;
import com.wxj.hbys.rxbus.RxBus;
import com.wxj.hbys.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
    @BindView(R.id.tv_register)
    TextView tv_register; // 注册

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_login, R.id.tv_register})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();

                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

                break;
        }
    }

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
        LoginRegisterNetwork
                .getLoginApi()
                .getLoginBean(username, password, Constant.PLATFORM_CLIENT)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        RxBus.getDefault().post("loginSuccess");
                        ToastUtils.show(mContext, "登录失败，服务器异常");
                    }

                    @Override
                    public void onNext(LoginResponse res) {
                        if (res.code == 200) {
                            LogUtils.e("请求到的key是：" + res.data.key + "=======" + res.data.userid);
                            RxBus.getDefault().post("loginSuccess");
                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }


}
