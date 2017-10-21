package com.help.reward.wxapi;

import com.help.reward.R;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.BooleanRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        mContext = this;
        api = WXAPIFactory.createWXAPI(this, Constant.WXCHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        // 0 成功 -1 错误 -2 取消
        LogUtils.e("onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            if (code == 0) { // 支付成功
                ToastUtils.show(mContext, "支付成功");
                //刷新当前数据,发送给MyOrderAllFragment
                RxBus.getDefault().post(new BooleanRxbusType(true));
                finish();

            } else if (code == -1) { // 错误
                ToastUtils.show(mContext, "支付失败");
                finish();

            } else if (code == -2) { // 用户取消
                ToastUtils.show(mContext, "支付失败");
                finish();

            }
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();*/
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}