package com.help.reward.activity.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.help.reward.App;
import com.help.reward.activity.BaseActivity;
import com.help.reward.activity.PayTypeActivity;
import com.help.reward.bean.Response.PayTypeAlipayResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.pay.alipay.OrderInfoUtil2_0;
import com.help.reward.pay.alipay.PayResult;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 17-8-29.
 */

public class AlipayActivity extends Activity {

    private Context mContext;
    private String pay_sn; // 支付单号
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtils.e("支付宝支付的返回值" + resultStatus + "----" + resultInfo);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.show(mContext, "支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.show(mContext, "支付失败");
                    }
                    finish();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        pay_sn = getIntent().getStringExtra("pay_sn");
        requestAliPay();

    }

    /***
     * 支付宝支付请求接口
     */
    private void requestAliPay() {
        if (App.APP_CLIENT_KEY == null || TextUtils.isEmpty(pay_sn)) {
            return;
        }
        LogUtils.e("点击支付宝支付...." + pay_sn + "======" + App.APP_CLIENT_KEY);
        ShopcartNetwork.getShopcartCookieApi()
                .getBalancePayTypeAliPayResponse(pay_sn, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PayTypeAlipayResponse>() {
                    @Override
                    public void onNext(PayTypeAlipayResponse response) {
                        if (response.code == 200) {
                            LogUtils.e("返回数据是:" + response.data.sign + "" +
                                    "--" + response.data.app_id
                                    + "--" + response.data.biz_content
                                    + "--" + response.data.charset
                                    + "--" + response.data.method
                                    + "--" + response.data.sign_type
                                    + "--" + response.data.timestamp
                                    + "--" + response.data.version
                            );
                            sendAliPay(response.data);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 发送支付宝支付
     */
    private void sendAliPay(final PayTypeAlipayResponse.PayTypeAlipayBean bean) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(bean.app_id, bean.biz_content, bean.charset,
                            bean.method, bean.notify_url, bean.timestamp, bean.version, bean.sign_type);
                    String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

                    String sign = URLEncoder.encode(bean.sign, "UTF-8");
                    if (!sign.startsWith("sign=")) {
                        sign = "sign=" + sign;
                    }
                    final String orderInfo = orderParam + "&" + sign;
                    LogUtils.e("订单信息是:" + orderInfo);

                    PayTask alipay = new PayTask(AlipayActivity.this);
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    LogUtils.e("msp:" + result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
