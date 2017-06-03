package com.help.reward.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.help.reward.App;
import com.help.reward.activity.LoginActivity;
import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.bean.Response.LoginResponse2;
import com.help.reward.bean.Response.WXLoginTokenResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.WeiXinLoginRxbusType;
import com.help.reward.utils.Constant;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 17-5-25.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LogUtils.e("执行WXEntryActivity");
            mContext = getApplicationContext();
            api = WXAPIFactory.createWXAPI(this, Constant.WXCHAT_APP_ID, true);
            api.registerApp(Constant.WXCHAT_APP_ID); // 将应用注册到微信
            api.handleIntent(getIntent(), this);

        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        LogUtils.e("执行onNewIntent");
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.e("当前的req是" + baseReq.getType());
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e("onResp返回数据是" + resp.errStr + "----" + resp.errCode);
        int code = resp.errCode;
        switch (code) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    LogUtils.e("code是：" + sendResp.code);
                    // 拿到code 再去获取access_token
                    getAccessToken(sendResp.code);
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 用户取消
                LogUtils.e("点击取消");
//                WXEntryActivity.this.setResult(RESULT_CANCELED);
                WXEntryActivity.this.finish();

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                // 发送被拒绝
                LogUtils.e("点击拒绝");
//                WXEntryActivity.this.setResult(RESULT_CANCELED);
                WXEntryActivity.this.finish();

                break;
            default:
//                WXEntryActivity.this.setResult(RESULT_CANCELED);
                WXEntryActivity.this.finish();

                break;
        }

    }

    /***
     * 获取token
     * ?act=connect_wx&op=index
     * "connect_wx", "index",
     *
     * @param code
     */
    private void getAccessToken(String code) {

        PersonalNetwork
                .getLoginApi()
                .getWXLoginTokenResponse(code)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<WXLoginTokenResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(WXLoginTokenResponse res) {
//                        LogUtils.e("返回的信息是：" + res + "=====" + res.toString());
                        if (res.code == 200) {
//                            LogUtils.e("返回信息是：" + res.data.access_token + "--" + res.data.openid + "--" + Constant.PLATFORM_CLIENT);
                            if (res.data.access_token != null) {
                                login(res.data.access_token, res.data.openid, Constant.PLATFORM_CLIENT);
                            }

                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }


    /***
     * ?act=connect_wx&op=login
     * "connect_wx", "login",
     *
     * @param access_token
     * @param openid
     * @param platformClient
     */
    private void login(String access_token, String openid, String platformClient) {
        PersonalNetwork
                .getLoginApi()
                .getWXLoginResponse(access_token, openid, platformClient)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<LoginResponse2>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(LoginResponse2 res) {
                        LogUtils.e("微信登录成功...." + res.code + "----res" + res);
                        if (res.code == 200) {
                            LogUtils.e("res.data.key=" + res.data.key);
                            // 登录成功
                            App.APP_CLIENT_KEY = res.data.key;
                            App.APP_USER_ID = res.data.userid;
                            // 请求会员信息
                            App.mLoginReponse = res.data;
                            RxBus.getDefault().post("loginSuccess");
                            // 关闭LoginActivity页面
                            RxBus.getDefault().post(new WeiXinLoginRxbusType(true));

                            WXEntryActivity.this.finish();

                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(api != null){
            api.unregisterApp();
        }
    }
}
