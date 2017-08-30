package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.WeiXinLoginRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.help.reward.utils.StatusBarUtil;
import com.help.reward.view.MyProcessDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

    private Subscription subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd, R.id.tv_look})
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
                Intent mIntent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                mIntent.putExtra("title", "忘记密码");
                startActivity(mIntent);
                ActivitySlideAnim.slideInAnim(LoginActivity.this);

                break;
            case R.id.tv_look:
                finish();
                ActivitySlideAnim.slideOutAnim(LoginActivity.this);

                break;

            /*, R.id.tv_wetchat, R.id.tv_qq, R.id.tv_weibo
            case R.id.tv_wetchat:
                // 微信登录
                wxLogin();

                break;
            case R.id.tv_qq:

                break;
            case R.id.tv_weibo:

                break;*/
        }
    }

    /***
     * 微信登录
     */
    private void wxLogin() {
        getLoginSuccessInfo();
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WXCHAT_APP_ID, true);
        api.registerApp(Constant.WXCHAT_APP_ID); // 将应用注册到微信
        if (api != null && api.isWXAppInstalled()) {

            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo"; // 作用域 只获取用户信息
            req.state = String.valueOf(System.currentTimeMillis());
            api.sendReq(req);

        } else {
            ToastUtils.show(getApplicationContext(), "请安装微信");
        }
    }

    private Subscription subscribeRxbusWeixin;

    private void getLoginSuccessInfo() {
        subscribeRxbusWeixin = RxBus.getDefault().toObservable(WeiXinLoginRxbusType.class).subscribe(new Action1<WeiXinLoginRxbusType>() {
            @Override
            public void call(WeiXinLoginRxbusType type) {
                if (type.isLogin) {
                    finish();
                    ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                }
                if (!subscribeRxbusWeixin.isUnsubscribed()) {
                    subscribeRxbusWeixin.unsubscribe();
                }
            }
        });
    }


    /***
     * 登录逻辑
     */
    private void login() {
        final String username = et_login_phone_number.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(mContext, "请输入用户名");
            return;
        }
        final String password = et_login_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(mContext, "请输入密码");
            return;
        }

        MyProcessDialog.showDialog(mContext);
        subscribe = PersonalNetwork
                .getLoginApi()
                .getLoginBean(username, password, App.GETUI_CLIENT_ID, Constant.PLATFORM_CLIENT).subscribeOn(Schedulers.io()) // 请求放在io线程中
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
                        if (res.code == 200) {
                            LogUtils.e("请求到的key是：" + res.data.key + "=======" + res.data.userid);
                            App.APP_CLIENT_KEY = res.data.key;
                            App.APP_USER_ID = res.data.userid;
                            // 请求会员信息
                            App.mLoginReponse = res.data;
                            RxBus.getDefault().post("loginSuccess");
                            LogUtils.e("用户名是:" + App.mLoginReponse.easemobId + "----密码是:" + password);
                            LoginToHuanxin(App.mLoginReponse.easemobId, password);
                        } else {
                            MyProcessDialog.closeDialog();
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }


    private void LoginToHuanxin(String username, String password) {
        EMClient.getInstance().login(username, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                MyProcessDialog.closeDialog();
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                LogUtils.e("onSuccess--登录聊天服务器成功！");

                finishActivity();
            }

            @Override
            public void onProgress(int progress, String status) {
                LogUtils.e("onProgress中" + progress + "--status" + status);
            }

            @Override
            public void onError(int code, String message) {
                MyProcessDialog.closeDialog();
//                ToastUtils.show(LoginActivity.this,"登录聊天服务器失败！");
                LogUtils.e("onError  登录聊天服务器失败！");

                finishActivity();
            }
        });
    }

    private void finishActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.finish();
                ActivitySlideAnim.slideOutAnim(LoginActivity.this);
            }
        });
    }

    @BindView(R.id.ll_total)
    LinearLayout ll_total;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(LoginActivity.this, 0, ll_total);
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
