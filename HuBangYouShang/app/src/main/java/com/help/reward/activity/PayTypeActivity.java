package com.help.reward.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.PayTypeAdapter;
import com.help.reward.bean.Response.PayTypeAlipayResponse;
import com.help.reward.bean.Response.PayTypeResponse;
import com.help.reward.bean.Response.PayTypeWchatResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.pay.alipay.OrderInfoUtil2_0;
import com.help.reward.pay.alipay.PayResult;
import com.help.reward.utils.ActivityManager;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择支付方式
 * Created by MXY on 2017/2/22.
 */

public class PayTypeActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_paytype_money)
    TextView tvPaytypeMoney;

    @BindView(R.id.layout_paytype_wchat)
    RelativeLayout layoutPaytypeWchat;
    @BindView(R.id.layout_paytype_alipay)
    RelativeLayout layoutPaytypeAlipay;
    @BindView(R.id.layout_paytype_yinlian)
    RelativeLayout layoutPaytypeYinlian;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private PayTypeAdapter mPayTypeAdapter;

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
                        Toast.makeText(PayTypeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayTypeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
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
        setContentView(R.layout.activity_paytype);
        ButterKnife.bind(this);

        pay_sn = getIntent().getStringExtra("pay_sn");
        boolean removeShopcatAndConfirmOrderActivity = getIntent().getBooleanExtra("removeShopcatAndConfirmOrderActivity",false);
        if(removeShopcatAndConfirmOrderActivity){
            ActivityManager.getActivityManager().finishActivity(ConfirmOrderActivity.class);
            ActivityManager.getActivityManager().finishActivity(ShopcartActivity.class);
        }

        initView();
        initNet();
    }

    private void initNet() {
        if (App.APP_CLIENT_KEY == null || TextUtils.isEmpty(pay_sn)) {
            return;
        }
        ShopcartNetwork.getShopcartCookieApi()
                .getPayTypeResponse(pay_sn, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PayTypeResponse>() {
                    @Override
                    public void onNext(PayTypeResponse payTypeResponse) {
                        if (payTypeResponse.code == 200) {
                            pay_sn = payTypeResponse.data.pay_sn;
                            tvPaytypeMoney.setText("￥" + payTypeResponse.data.api_pay_amount);
                            // 显示各种订单
                            mPayTypeAdapter.setDataList(payTypeResponse.data.order_list);

                        } else {
                            ToastUtils.show(mContext, payTypeResponse.msg);
                        }
                    }
                });
    }

    private void initView() {
        tvTitle.setText("选择支付方式");
        tvTitleRight.setVisibility(View.GONE);

        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mPayTypeAdapter = new PayTypeAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mPayTypeAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        lRecyclerview.setPullRefreshEnabled(false);
        lRecyclerview.setLoadMoreEnabled(false);
    }

    @OnClick({R.id.iv_title_back, R.id.layout_paytype_wchat, R.id.layout_paytype_alipay, R.id.layout_paytype_yinlian})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(PayTypeActivity.this);

                break;
            case R.id.layout_paytype_wchat:
                // 请求接口
                requestWchatPay();

                break;
            case R.id.layout_paytype_alipay:
                // 支付宝支付
                requestAliPay();

                break;
            case R.id.layout_paytype_yinlian:

                break;

        }
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
                .getPayTypeAliPayResponse(pay_sn, App.APP_CLIENT_KEY)
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

                    PayTask alipay = new PayTask(PayTypeActivity.this);
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


    /***
     * 微信支付请求接口
     */
    private void requestWchatPay() {
        if (App.APP_CLIENT_KEY == null || TextUtils.isEmpty(pay_sn)) {
            return;
        }
        ShopcartNetwork.getShopcartCookieApi()
                .getPayTypeWchatResponse(pay_sn, App.APP_CLIENT_KEY)
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
            IWXAPI api = WXAPIFactory.createWXAPI(PayTypeActivity.this, Constant.WXCHAT_APP_ID);
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
            } else {
                ToastUtils.show(getApplicationContext(), "请安装微信");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(mContext, "支付出错");
        }
    }

}
