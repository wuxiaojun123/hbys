package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.PayTypeAdapter;
import com.help.reward.bean.Response.PayTypeResponse;
import com.help.reward.bean.Response.PayTypeWchatResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytype);
        ButterKnife.bind(this);

        pay_sn = getIntent().getStringExtra("pay_sn");

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

                break;
            case R.id.layout_paytype_yinlian:

                break;

        }
    }

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
