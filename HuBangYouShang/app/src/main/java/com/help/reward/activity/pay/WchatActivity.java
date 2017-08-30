package com.help.reward.activity.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.help.reward.App;
import com.help.reward.activity.PayTypeActivity;
import com.help.reward.bean.Response.PayTypeWchatResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.Constant;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 17-8-29.
 */

public class WchatActivity extends Activity {

    private Context mContext;
    private String pay_sn; // 支付单号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        pay_sn = getIntent().getStringExtra("pay_sn");
        requestWchatPay();
    }

    /***
     * 微信支付请求接口
     */
    private void requestWchatPay() {
        if (App.APP_CLIENT_KEY == null || TextUtils.isEmpty(pay_sn)) {
            return;
        }
        ShopcartNetwork.getShopcartCookieApi()
                .getBalancePayTypeWchatResponse(pay_sn, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PayTypeWchatResponse>() {
                    @Override
                    public void onNext(PayTypeWchatResponse response) {
                        if (response.code == 200) {
                            LogUtils.e("返回数据是:" + response.data.appid + "" +
                                    "--" + response.data.noncestr
                                    + "--" + response.data.packagestr
                                    + "--" + response.data.prepayid
                                    + "--" + response.data.sign
                                    + "--" + response.data.timestamp);

                            sendPay(response.data);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 发送支付结果
     *
     * @param bean
     */
    private void sendPay(PayTypeWchatResponse.PayTypeWchatBean bean) {
        try {
            IWXAPI api = WXAPIFactory.createWXAPI(WchatActivity.this, Constant.WXCHAT_APP_ID);
            api.registerApp(Constant.WXCHAT_APP_ID); // 将应用注册到微信
            if (api != null && api.isWXAppInstalled()) {
                PayReq req = new PayReq();
                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                req.appId = bean.appid;
                req.partnerId = bean.partnerid;
                req.prepayId = bean.prepayid;
                req.nonceStr = bean.noncestr;
                req.timeStamp = bean.timestamp;
                req.packageValue = bean.packagestr;
                req.sign = bean.sign;
                // req.extData			= "app data"; // optional
                ToastUtils.show(mContext, "正常调起支付");
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
                // 清除当前页
                finish();
            } else {
                ToastUtils.show(getApplicationContext(), "请安装微信");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(mContext, "支付出错");
        }
    }


}
