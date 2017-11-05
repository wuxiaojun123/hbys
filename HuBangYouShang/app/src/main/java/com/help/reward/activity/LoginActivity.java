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
import com.help.reward.utils.SharedPreferenceConstant;
import com.help.reward.utils.StatusBarUtil;
import com.help.reward.view.MyProcessDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.idotools.utils.LogUtils;
import com.idotools.utils.SharedPreferencesHelper;
import com.idotools.utils.ToastUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
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

        String defaultUsername = SharedPreferencesHelper.getInstance(mContext).getString(SharedPreferenceConstant.KEY_LOGIN_USERNAME, null);
        et_login_phone_number.setText(defaultUsername);
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd, R.id.tv_look, R.id.tv_wetchat, R.id.tv_qq, R.id.tv_weibo})
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

            case R.id.tv_wetchat:
                // 微信登录
                wxLogin();

                break;
            case R.id.tv_qq:
                // qq登陆
                qqLogin();

                break;
            case R.id.tv_weibo:

                break;
        }
    }

    private Tencent mTencent;
    private BaseUiListener iBaseUiListener;

    /***
     * qq登陆
     */
    private void qqLogin() {
        mTencent = Tencent.createInstance(Constant.QQ_LOGIN_APP_ID, this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            iBaseUiListener = new BaseUiListener();
            mTencent.login(this, "all", iBaseUiListener);
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
                            LogUtils.e("用户名是:" + App.mLoginReponse.easemobId + "----密码是:" + password + App.mLoginReponse.new_message);
                            SharedPreferencesHelper.getInstance(mContext).putString(SharedPreferenceConstant.KEY_LOGIN_USERNAME, username);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            LogUtils.e("执行 onComplete" + o);
            JSONObject jsonObject = (JSONObject) o;

            //设置openid和token，否则获取不到下面的信息
            initOpenidAndToken(jsonObject);
            //获取QQ用户的各信息
            getUserInfo();
        }

        @Override
        public void onError(UiError e) {
            LogUtils.e("code:" + e.errorCode + ", msg:" + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            LogUtils.e("onCancel");
        }

    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String openid = jsonObject.getString("openid");
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");

            LogUtils.e("openid=" + openid + "--token=" + token + "--" + expires);

            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
        QQToken mQQToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(LoginActivity.this, mQQToken);
        userInfo.getUserInfo(new IUiListener() {
                                 @Override
                                 public void onComplete(final Object o) {
                                     try {
                                         JSONObject userInfoJson = (JSONObject) o;
                                         String nickname = userInfoJson.getString("nickname");//直接传递一个昵称的内容过去
                                         String headUrl = null;
                                         if (userInfoJson.has("figureurl")) {
                                             headUrl = userInfoJson.getString("figureurl_qq_2");
                                         }
                                         LogUtils.e("昵称是：" + nickname + "------" + headUrl);
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                     }
                                 }

                                 @Override
                                 public void onError(UiError uiError) {
                                     LogUtils.e("GET_QQ_INFO_ERROR 获取qq用户信息错误");
                                 }

                                 @Override
                                 public void onCancel() {
                                     LogUtils.e("GET_QQ_INFO_CANCEL 获取qq用户信息取消");
                                 }
                             }
        );
    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
